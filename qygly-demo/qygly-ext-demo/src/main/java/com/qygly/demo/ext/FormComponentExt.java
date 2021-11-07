package com.qygly.demo.ext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qygly.demo.ext.entity.*;
import com.qygly.ext.jar.helper.ExtJarHelper;
import com.qygly.shared.QyglyException;
import com.qygly.shared.util.SharedUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author daiguanjun
 * @date 2021-11-2
 */
public class FormComponentExt {
    public void selectFormById() {
        Map<String, Object> paramMap = ExtJarHelper.extApiParamMap.get();
        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();
        List<Map<String, Object>> results = jdbcTemplate.queryForList("select a.id, a.code, a.name, a.ap_doc_mp_id masterId, a.is_mp isMaster,a.remark, b.name type, b.id typeId from AP_DOC a inner join ap_doc_type b on a.ap_doc_type_id = b.id where a.ID = ?", paramMap.get("id"));
        Map<String, Object> resultMap;
        FormDetail formDetail;
        if (SharedUtil.isEmptyList(results)) {
            throw new QyglyException("不存在此表单");
        } else {
            resultMap = results.get(0);
            Object docId = resultMap.get("id");
            Object masterId = resultMap.get("masterId");
            String masterName = "";
            if (!Objects.isNull(masterId)) {
                masterName = (String) jdbcTemplate.queryForList("select name from ap_doc where id = ?", masterId).get(0).get("name");
            }
            formDetail = new FormDetail(docId, resultMap.get("name"),resultMap.get("code"), masterId, masterName, resultMap.get("typeId"), resultMap.get("type"), resultMap.get("isMaster"), resultMap.get("remark"));
            Object verId = jdbcTemplate.queryForList("select id from ap_doc_ver where ap_doc_id = ? order by ver_no desc limit 1", docId).get(0).get("id");
            List<Map<String, Object>> regions = jdbcTemplate.queryForList("select a.id regionId, a.is_from_mp isFromMaster, b.name regionName, b.id regionTypeId, a.seq_no as seqNo from ap_doc_region a inner join " +
                    "ap_doc_region_type b on a.ap_doc_region_type_id = b.id where a.ap_doc_ver_id = ? order by a.seq_no", verId);
            List<Region> regionList = new ArrayList<>();
            for (Map<String, Object> regionMap : regions) {
                Object regionId = regionMap.get("regionId");
                String regionName = (String) regionMap.get("regionName");
                Integer regionType = Integer.parseInt((String) regionMap.get("regionTypeId"));
                Region region = new Region(regionType, regionId, regionMap.get("isFromMaster"), regionName, regionMap.get("seqNo"));
                List<Map<String, Object>> fieldMaps = jdbcTemplate.queryForList("select a.seq_no seqNo, a.is_mandatory required, b.name fieldName,b.id fieldId, b.code fieldCode, c.name fieldType from ap_doc_region_field a" +
                        " inner join ap_field b on a.ap_field_id = b.id inner join ap_field_type c on b.ap_field_type_id = c.id where a.ap_doc_region_id = ? order by a.seq_no", regionId);
                List<Map<String, Object>> instances = jdbcTemplate.queryForList("select id from ap_doc_inst where ap_doc_id = ? and ap_doc_ver_id = ?",
                        paramMap.get("id"), verId);
                List<Field> fields = new ArrayList<>();
                for (Map<String, Object> map : fieldMaps) {
                    Field field = new Field(map.get("fieldId"), map.get("fieldName"), map.get("fieldType"), map.get("fieldCode"), map.get("required"), map.get("seqNo"));
                    fields.add(field);
                }
                List<Map<String, String>> rows = null;
                if (!SharedUtil.isEmptyList(instances)) {
                    Object instanceId = instances.get(0).get("id");
                    List<Map<String, Object>> values = jdbcTemplate.queryForList("select field_value from ap_doc_inst_field_value where ap_doc_inst_id = ? and ap_doc_region_id = ? order by field_seq_no", instanceId, regionId);
                    if (SharedUtil.isEmptyList(values)) {
                        continue;
                    }
                    if (regionType == 1) {
                        rows = dealTableData(values, fieldMaps);
                    } else {
                        rows = dealFormData(values, fieldMaps);
                    }
                }
                region.setFields(fields);
                region.setRows(rows);
                regionList.add(region);
            }
            formDetail.setRegions(regionList);

        }
        String str = JSON.toJSONString(formDetail, SerializerFeature.WriteMapNullValue);
        Map<String, Object> res = new HashMap<>(2);
        res.put("data", str);
        ExtJarHelper.returnValue.set(res);
    }

    private List<Map<String, String>> dealFormData(List<Map<String, Object>> values, List<Map<String, Object>> fieldMaps) {
        List<Map<String, String>> rows = new ArrayList<>();
        rows.add(new HashMap<>());
        for (int i = 0; i < values.size(); i++) {
            rows.get(0).put((String) fieldMaps.get(i).get("fieldCode"), (String) values.get(i).get("field_value"));
        }
        return rows;
    }

    private List<Map<String, String>> dealTableData(List<Map<String, Object>> values, List<Map<String, Object>> fieldMaps) {
        List<Map<String, String>> rows = new ArrayList<>();
        for (int j = 0; j < values.size(); j++) {
            String colValue = (String) values.get(j).get("field_value");
            String[] cols = colValue.split(",");
            for (int i = 0; i < cols.length; i++) {
                if (rows.size() <= i) {
                    rows.add(new HashMap<>());
                }
                rows.get(i).put((String) fieldMaps.get(j).get("fieldCode"), cols[i]);
            }
        }
        return rows;
    }

//    public void updateForm() {
//        Map<String, Object> paramMap = ExtJarHelper.extApiParamMap.get();
//        Object param = paramMap.get("FormCreateModel");
//        FormCreateModel formCreateModel = JSON.parseObject((String) param, FormCreateModel.class);
//        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();
//        jdbcTemplate.update("update ap_doc T set T.name = ?, T.remark = ?, T.ap_doc_type_id = ?, T.ap_doc_mp_id = ? where id = ?",
//                formCreateModel.getFormName(), formCreateModel.getFormInstructions(), formCreateModel.getTypeId(), formCreateModel.getMasterId(), formCreateModel.getId());
//        Map<String, Object> versionMap = jdbcTemplate.queryForList("select * from ap_doc_ver where ap_doc_id = ? order by ver_no desc limit 1", formCreateModel.getId()).get(0);
//        String apDocVerMBId = jdbcTemplate.queryForObject("select AP_DOC_VER.ID from AP_DOC_VER where AP_DOC_VER.AP_DOC_ID=" + "\""+formCreateModel.getMasterId() + "\" order by ver_no desc limit 1", String.class);
//        String apDocVerId = ExtJarHelper.insertData("AP_DOC_VER");
//        Integer ver = (Integer) versionMap.get("ver_no") + 1;
//        jdbcTemplate.update("UPDATE AP_DOC_VER " +
//                        "T SET T.NAME=?,T.IS_MP=0," +
//                        "T.VER_NO=?," +
//                        "T.IS_CURRENT_VER=1," +
//                        "T.AP_DOC_VER_MP_ID=?," +
//                        "T.AP_DOC_ID=? " +
//                        "WHERE T.ID=?",
//                formCreateModel.getFormName(), ver, apDocVerMBId, formCreateModel.getId(), apDocVerId);
//        insertData(jdbcTemplate, formCreateModel, apDocVerId);
//
//        Map<String, Object> res = new HashMap<>(2);
//        ExtJarHelper.returnValue.set(res);
//    }
//
//    public void createDoc() {
//        Map<String, Object> paramMap = ExtJarHelper.extApiParamMap.get();
//        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();
//        Object param = paramMap.get("FormCreateModel");
//        FormCreateModel domain = JSON.parseObject((String) param, FormCreateModel.class);
//
//        String workNo = WorkNoGenerationExt2.createBDNo(ExtJarHelper.stringRedisTemplate.get());
//        domain.setFormNo(workNo);
//
//        String apDocId = ExtJarHelper.insertData("AP_DOC");
//        jdbcTemplate.update("UPDATE AP_DOC T SET " +
//                        "T.CODE=?, " +
//                        "T.NAME=?," +
//                        "T.AP_DOC_TYPE_ID=?," +
//                        "T.IS_MP=?," +
//                        "T.REMARK=?," +
//                        "T.AP_DOC_MP_ID=?," +
//                        "T.START_STATUS=? " +
//                        "WHERE T.ID=?",
//                domain.getFormNo(), domain.getFormName(), domain.getTypeId(), 0, domain.getFormInstructions(), domain.getMasterId(), 0, apDocId);
//        domain.setId(apDocId);
//        String apDocVerId = ExtJarHelper.insertData("AP_DOC_VER");
//
//        String apDocVerMBId = jdbcTemplate.queryForObject("select AP_DOC_VER.ID from AP_DOC_VER where AP_DOC_VER.AP_DOC_ID=" + "\""+domain.getMasterId() + "\" order by ver_no desc limit 1", String.class);
//        jdbcTemplate.update("UPDATE AP_DOC_VER " +
//                        "T SET T.NAME=?," +
//                        "T.IS_MP=0," +
//                        "T.VER_NO=1," +
//                        "T.IS_CURRENT_VER=1," +
//                        "T.AP_DOC_VER_MP_ID=?," +
//                        "T.AP_DOC_ID=? " +
//                        "WHERE T.ID=?",
//                domain.getFormName(), apDocVerMBId, apDocId, apDocVerId);
//
//        insertData(jdbcTemplate, domain, apDocVerId);
//
//        Map<String, Object> res = new HashMap<>(2);
//        ExtJarHelper.returnValue.set(res);
//    }
//
//    private void insertData(JdbcTemplate jdbcTemplate, FormCreateModel formCreateModel, String apDocVerId) {
//        String apDocInstId = ExtJarHelper.insertData("AP_DOC_INST");
//        jdbcTemplate.update("UPDATE AP_DOC_INST T SET " +
//                        "T.AP_DOC_ID=?, " +
//                        "T.AP_DOC_VER_ID=? " +
//                        "WHERE T.ID=?",
//                formCreateModel.getId(), apDocVerId, apDocInstId);
//
//        List<FormInfoModel> regionList = formCreateModel.getRegions();
//
//        regionList.forEach(region -> {
//            String apDocRegionFormInfoId = ExtJarHelper.insertData("AP_DOC_REGION");
//            jdbcTemplate.update("UPDATE AP_DOC_REGION T SET " +
//                            "T.AP_DOC_REGION_TYPE_ID=?, " +
//                            "T.SEQ_NO=?," +
//                            "T.AP_DOC_VER_ID=?," +
//                            "T.IS_FROM_MP=? " +
//                            "WHERE T.ID=?",
//                    region.getRegionType(), region.getOrder(), apDocVerId, region.getIsFromMaster(), apDocRegionFormInfoId);
//
//            List<FieldInfoModel> fields = region.getFields();
//
//            Map<String, String> valueMap = fields.stream().filter(distinctByKey(field -> field.getFieldId())).collect(Collectors.toMap(FieldInfoModel::getFieldId, FieldInfoModel::getEmptyString));
//            Map<String, FieldInfoModel> fieldMap = new HashMap<>();
//            for (FieldInfoModel field : fields) {
//                if (!fieldMap.containsKey(field.getFieldId())) {
//                    fieldMap.put(field.getFieldId(), field);
//                }
//                if (region.getRegionType() == 1) {
//                    valueMap.put(field.getFieldId(), valueMap.getOrDefault(field.getFieldId(), "") + field.getFieldValue() + ",");
//                } else {
//                    valueMap.put(field.getFieldId(), field.getFieldValue());
//                }
//            }
//
//            fieldMap.values().forEach(field -> {
//                String apDocRegionFieldId = ExtJarHelper.insertData("AP_DOC_REGION_FIELD");
//                jdbcTemplate.update("UPDATE AP_DOC_REGION_FIELD T SET " +
//                                "T.AP_FIELD_ID=?, " +
//                                "T.IS_EDITABLE=?, " +
//                                "T.IS_MANDATORY=?, " +
//                                "T.SEQ_NO=?, " +
//                                "T.AP_DOC_REGION_ID=? " +
//                                "WHERE T.ID=?",
//                        field.getFieldId(), 1, field.getIsMandatory(), field.getOrder(), apDocRegionFormInfoId, apDocRegionFieldId);
//                String apDocInstFieldValueId = ExtJarHelper.insertData("AP_DOC_INST_FIELD_VALUE");
//                String value = valueMap.get(field.getFieldId());
//                if (region.getRegionType() == 1) {
//                    value = value.substring(0, value.length() - 1);
//                }
//                jdbcTemplate.update("UPDATE AP_DOC_INST_FIELD_VALUE T SET " +
//                                "T.AP_DOC_ID=?, " +
//                                "T.AP_DOC_VER_ID=?," +
//                                "T.AP_DOC_REGION_ID=?," +
//                                "T.AP_DOC_REGION_FIELD_ID=?," +
//                                "T.AP_FIELD_ID=?," +
//                                "T.FIELD_VALUE=?," +
//                                "T.REGION_SEQ_NO=?," +
//                                "T.AP_DOC_INST_ID=?," +
//                                "T.FIELD_SEQ_NO=? " +
//                                "WHERE T.ID=?",
//                        formCreateModel.getId(), apDocVerId, apDocRegionFormInfoId, apDocRegionFieldId, field.getFieldId(), value, region.getOrder(), apDocInstId, field.getOrder(), apDocInstFieldValueId);
//            });
//
//        });
//    }
//
//    private void setValue(Field field, Map<String, Object> map, Object value) {
//        field.setFieldName(map.get("fieldName"));
//        field.setFieldId(map.get("fieldId"));
//        field.setType(map.get("fieldTypeName"));
//        field.setField(map.get("fieldType"));
//        field.setFieldValue(value);
//        field.setOrder(map.get("seqNo"));
//        field.setRequired(map.get("required"));
//    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
