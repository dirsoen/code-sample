package com.qygly.demo.ext.form;

import com.alibaba.fastjson.JSON;
import com.qygly.demo.ext.WorkNoGenerationExt;
import com.qygly.demo.ext.form.model.FieldInfoModel;
import com.qygly.demo.ext.form.model.FormCreateModel;
import com.qygly.ext.jar.helper.ExtJarHelper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuzhifeng
 * @Date 2021/11/2 2:44 下午
 */
public class ApDocExt {
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public void createForm() {
        Map<String, Object> param = ExtJarHelper.extApiParamMap.get();
        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();

        FormCreateModel domain = JSON.parseObject((String) param.get("FormCreateModel"), FormCreateModel.class);
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        Integer var = 1;

        String workNo = WorkNoGenerationExt.createBDWorkNo(ExtJarHelper.stringRedisTemplate.get());
        domain.setFormNo(workNo);

        String apDocId = ExtJarHelper.insertData("AP_DOC");
        jdbcTemplate.update("UPDATE AP_DOC T SET " +
                        "T.CODE=?, " +
                        "T.NAME=?," +
                        "T.AP_DOC_TYPE_ID=?," +
                        "T.IS_MP=?," +
                        "T.REMARK=?," +
                        "T.AP_DOC_MP_ID=?," +
                        "T.START_STATUS=? " +
                        "WHERE T.ID=?",
                domain.getFormNo(), domain.getFormName(), domain.getFormType(), 0, domain.getFormInstructions(), domain.getMasterMask(), 0, apDocId);

        String apDocVerId = ExtJarHelper.insertData("AP_DOC_VER");

        String apDocVerMBId = jdbcTemplate.queryForObject("select AP_DOC_VER.ID from AP_DOC_VER where AP_DOC_VER.AP_DOC_ID=\"" + domain.getMasterMask() + "\"", String.class);
        jdbcTemplate.update("UPDATE AP_DOC_VER " +
                        "T SET T.NAME=?," +
                        "T.IS_MP=?," +
                        "T.VER_NO=?," +
                        "T.IS_CURRENT_VER=?," +
                        "T.AP_DOC_VER_MP_ID=?," +
                        "T.AP_DOC_ID=? " +
                        "WHERE T.ID=?",
                domain.getFormName() + "-" + var.toString(), 0, var.toString(), 1, apDocVerMBId, apDocId, apDocVerId);

        String apDocInstId = ExtJarHelper.insertData("AP_DOC_INST");
        jdbcTemplate.update("UPDATE AP_DOC_INST T SET " +
                        "T.AP_DOC_ID=?, " +
                        "T.AP_DOC_VER_ID=? " +
                        "WHERE T.ID=?",
                apDocId, apDocVerId, apDocInstId);

        List<FieldInfoModel> formInfoList = domain.getFormInfo().getFormInfoList();
        List<FieldInfoModel> customFieldList = domain.getFormInfo().getCustomFieldList();

        Map<Integer, List<FieldInfoModel>> formInfoListMapByRegion =
                formInfoList.stream().collect(Collectors.groupingBy(FieldInfoModel::getRegionOrder));
        Map<Integer, List<FieldInfoModel>> customFieldListMapByRegion =
                customFieldList.stream().collect(Collectors.groupingBy(FieldInfoModel::getRegionOrder));

        formInfoListMapByRegion.keySet().forEach(key -> {
            String apDocRegionFormInfoId = ExtJarHelper.insertData("AP_DOC_REGION");
            jdbcTemplate.update("UPDATE AP_DOC_REGION T SET " +
                            "T.AP_DOC_REGION_TYPE_ID=?, " +
                            "T.SEQ_NO=?," +
                            "T.AP_DOC_VER_ID=?" +
                            "WHERE T.ID=?",
                    key % 2, key, apDocVerId, apDocRegionFormInfoId);

            Map<String, List<FieldInfoModel>> formInfoMap =
                    formInfoListMapByRegion.get(key).stream().collect(Collectors.groupingBy(FieldInfoModel::getKey));

            Integer order = 0;
            for (String fieldKey : formInfoMap.keySet()) {
                List<FieldInfoModel> fieldInfoModels = formInfoMap.get(fieldKey);
                String values;
                if (fieldInfoModels.size() > 1) {
                    fieldInfoModels.sort(Comparator.comparing(FieldInfoModel::getOrder));
                    values = String.join(",", fieldInfoModels.stream().map(FieldInfoModel::getValue).collect(Collectors.toList()));
                } else {
                    values = fieldInfoModels.get(0).getValue();
                }

                Integer isMandatory = fieldInfoModels.get(0).getIsMandatory();
                String apDocRegionFieldId = ExtJarHelper.insertData("AP_DOC_REGION_FIELD");
                jdbcTemplate.update("UPDATE AP_DOC_REGION_FIELD T SET " +
                                "T.AP_FIELD_ID=?, " +
                                "T.IS_EDITABLE=?, " +
                                "T.IS_MANDATORY=?, " +
                                "T.SEQ_NO=?, " +
                                "T.AP_DOC_REGION_ID=? " +
                                "WHERE T.ID=?",
                        fieldKey, 1, isMandatory, order, apDocRegionFormInfoId, apDocRegionFieldId);

                String apDocInstFieldValueId = ExtJarHelper.insertData("AP_DOC_INST_FIELD_VALUE");
                jdbcTemplate.update("UPDATE AP_DOC_INST_FIELD_VALUE T SET " +
                                "T.AP_DOC_ID=?, " +
                                "T.AP_DOC_VER_ID=?," +
                                "T.AP_DOC_REGION_ID=?," +
                                "T.AP_DOC_REGION_FIELD_ID=?," +
                                "T.AP_FIELD_ID=?," +
                                "T.FIELD_VALUE=?," +
                                "T.REGION_SEQ_NO=?," +
                                "T.AP_DOC_INST_ID=?," +
                                "T.FIELD_SEQ_NO=? " +
                                "WHERE T.ID=?",
                        apDocId, apDocVerId, apDocRegionFormInfoId, apDocRegionFieldId, fieldKey, values, key, apDocInstId, order, apDocInstFieldValueId);
                order++;
            }
        });


        customFieldListMapByRegion.keySet().forEach(key -> {
            String apDocRegionCustomFieldId = ExtJarHelper.insertData("AP_DOC_REGION");
            jdbcTemplate.update("UPDATE AP_DOC_REGION T SET " +
                            "T.AP_DOC_REGION_TYPE_ID=?, " +
                            "T.SEQ_NO=?," +
                            "T.AP_DOC_VER_ID=? " +
                            "WHERE T.ID=?",
                    key % 2, key, apDocVerId, apDocRegionCustomFieldId);

            Map<String, List<FieldInfoModel>> customFieldMap =
                    customFieldListMapByRegion.get(key).stream().collect(Collectors.groupingBy(FieldInfoModel::getKey));

            Integer order = 0;
            for (String fieldKey : customFieldMap.keySet()) {
                List<FieldInfoModel> fieldInfoModels = customFieldMap.get(fieldKey);
                String values;
                if (fieldInfoModels.size() > 1) {
                    fieldInfoModels.sort(Comparator.comparing(FieldInfoModel::getOrder));
                    values = String.join(",", fieldInfoModels.stream().map(FieldInfoModel::getValue).collect(Collectors.toList()));
                } else {
                    values = fieldInfoModels.get(0).getValue();
                }

                String apDocRegionFieldId = ExtJarHelper.insertData("AP_DOC_REGION_FIELD");
                jdbcTemplate.update("UPDATE AP_DOC_REGION_FIELD T SET " +
                                "T.AP_FIELD_ID=?, " +
                                "T.IS_EDITABLE=?, " +
                                "T.IS_MANDATORY=?, " +
                                "T.SEQ_NO=?, " +
                                "T.AP_DOC_REGION_ID=? " +
                                "WHERE T.ID=?",
                        fieldKey, 1, fieldInfoModels.get(0).getIsMandatory(), order, apDocRegionCustomFieldId, apDocRegionFieldId);

                String apDocInstFieldValueId = ExtJarHelper.insertData("AP_DOC_INST_FIELD_VALUE");
                jdbcTemplate.update("UPDATE AP_DOC_INST_FIELD_VALUE T SET " +
                                "T.AP_DOC_ID=?, " +
                                "T.AP_DOC_VER_ID=?," +
                                "T.AP_DOC_REGION_ID=?," +
                                "T.AP_DOC_REGION_FIELD_ID=?," +
                                "T.AP_FIELD_ID=?," +
                                "T.FIELD_VALUE=?," +
                                "T.REGION_SEQ_NO=?," +
                                "T.AP_DOC_INST_ID=?," +
                                "T.FIELD_SEQ_NO=? " +
                                "WHERE T.ID=?",
                        apDocId, apDocVerId, apDocRegionCustomFieldId, apDocRegionFieldId, fieldKey, values, key, apDocInstId, order, apDocInstFieldValueId);
                order++;
            }
        });

        Map<String, Object> res = new HashMap<>();
        ExtJarHelper.returnValue.set(res);
    }
}
