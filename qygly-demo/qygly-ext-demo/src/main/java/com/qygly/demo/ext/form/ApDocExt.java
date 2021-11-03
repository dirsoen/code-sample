package com.qygly.demo.ext.form;

import com.alibaba.fastjson.JSON;
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

        String apDocId = ExtJarHelper.insertData("AP_DOC");
        jdbcTemplate.update("UPDATE AP_DOC T SET " +
                        "T.CODE=?, T.NAME=?," +
                        "T.AP_DOC_TYPE_ID=?," +
                        "T.IS_MP=?,T.REMARK=?," +
                        "T.AP_DOC_MP_ID=?," +
                        "T.LAST_MODI_USER_ID=?," +
                        "T.LAST_MODI_DT=?," +
                        "T.START_STATUS=? " +
                        "WHERE T.ID=?",
                domain.getFormNo(), domain.getFormName(), domain.getFormType(), 0, domain.getFormInstructions(), domain.getMasterMask(), 0, time, 0, apDocId);

        String apDocVerId = ExtJarHelper.insertData("AP_DOC_VER");
        jdbcTemplate.update("UPDATE AP_DOC_VER " +
                        "T SET T.NAME=?,T.IS_MP=?," +
                        "T.VER_NO=?," +
                        "T.IS_CURRENT_VER=?," +
                        "T.AP_DOC_VER_MP_ID=?," +
                        "T.AP_DOC_ID=? " +
                        "WHERE T.ID=?",
                domain.getFormName() + "-" + var.toString(), 0, var.toString(), domain.getMasterMask(), apDocId, apDocVerId);

        String apDocRegionFormInfoId = ExtJarHelper.insertData("AP_DOC_REGION");
        jdbcTemplate.update("UPDATE AP_DOC_REGION T SET " +
                        "T.AP_DOC_REGION_TYPE_ID=?, " +
                        "T.SEQ_NO=?," +
                        "T.AP_DOC_VER_ID=?" +
                        "WHERE T.ID=?",
                domain.getFormInfo().getFormInfoType(), 1, apDocVerId, apDocRegionFormInfoId);

        String apDocRegionCustomFieldId = ExtJarHelper.insertData("AP_DOC_REGION");
        jdbcTemplate.update("UPDATE AP_DOC_REGION T SET " +
                        "T.AP_DOC_REGION_TYPE_ID=?, " +
                        "T.SEQ_NO=?," +
                        "T.AP_DOC_VER_ID=? " +
                        "WHERE T.ID=?",
                domain.getFormInfo().getCustomFieldType(), 2, apDocVerId, apDocRegionCustomFieldId);

        Map<String, List<FieldInfoModel>> formInfoMap = domain.getFormInfo().getFormInfoList().stream().collect(Collectors.groupingBy(FieldInfoModel::getKey));

        Map<String, List<FieldInfoModel>> customFieldMap = domain.getFormInfo().getCustomFieldList().stream().collect(Collectors.groupingBy(FieldInfoModel::getKey));

        Integer order = 0;
        for (String key : formInfoMap.keySet()) {
            List<FieldInfoModel> fieldInfoModels = formInfoMap.get(key);
            fieldInfoModels.sort(Comparator.comparing(FieldInfoModel::getOrder));

            String values = String.join(",", fieldInfoModels.stream().map(FieldInfoModel::getValue).collect(Collectors.toList()));

            String apDocRegionFieldId = ExtJarHelper.insertData("AP_DOC_REGION_FIELD");
            jdbcTemplate.update("UPDATE AP_DOC_REGION_FIELD T SET " +
                            "T.AP_FIELD_ID=?, " +
                            "T.IS_MANDATORY=?, " +
                            "T.SEQ_NO, " +
                            "T.AP_DOC_REGION_ID=? " +
                            "WHERE T.ID=?",
                    key, fieldInfoModels.get(0).getIsMandatory(), order, apDocRegionFormInfoId, apDocRegionFieldId);

            String apDocInstFieldValueId = ExtJarHelper.insertData("AP_DOC_INST_FIELD_VALUE");
            jdbcTemplate.update("UPDATE AP_DOC_INST_FIELD_VALUE T SET " +
                            "T.AP_DOC_ID=?, " +
                            "T.AP_DOC_VER_ID=?," +
                            "T.AP_DOC_REGION_ID=?," +
                            "T.AP_DOC_REGION_FIELD_ID=?," +
                            "T.AP_FIELD_ID=?," +
                            "T.FIELD_VALUE=?," +
                            "T.REGION_SEQ_NO" +
                            "T.FIELD_SEQ_NO=? " +
                            "WHERE T.ID=?",
                    apDocId, apDocVerId, apDocRegionFormInfoId, apDocRegionFieldId, key, values, 0, order, apDocInstFieldValueId);
            order++;
        }
        order = 0;
        for (String key : customFieldMap.keySet()) {
            List<FieldInfoModel> fieldInfoModels = customFieldMap.get(key);
            fieldInfoModels.sort(Comparator.comparing(FieldInfoModel::getOrder));

            String values = String.join(",", fieldInfoModels.stream().map(FieldInfoModel::getValue).collect(Collectors.toList()));

            String apDocRegionFieldId = ExtJarHelper.insertData("AP_DOC_REGION_FIELD");
            jdbcTemplate.update("UPDATE AP_DOC_REGION_FIELD T SET " +
                            "T.AP_FIELD_ID=?, " +
                            "T.IS_MANDATORY=?, " +
                            "T.SEQ_NO, " +
                            "T.AP_DOC_REGION_ID=? " +
                            "WHERE T.ID=?",
                    key, customFieldMap.get(key).get(0).getIsMandatory(), order, apDocRegionFieldId, apDocRegionCustomFieldId);

            String apDocInstFieldValueId = ExtJarHelper.insertData("AP_DOC_INST_FIELD_VALUE");
            jdbcTemplate.update("UPDATE AP_DOC_INST_FIELD_VALUE T SET " +
                            "T.AP_DOC_ID=?, " +
                            "T.AP_DOC_VER_ID=?," +
                            "T.AP_DOC_REGION_ID=?," +
                            "T.AP_DOC_REGION_FIELD_ID=?," +
                            "T.AP_FIELD_ID=?," +
                            "T.FIELD_VALUE=?," +
                            "T.REGION_SEQ_NO" +
                            "T.FIELD_SEQ_NO=? " +
                            "WHERE T.ID=?",
                    apDocId, apDocVerId, apDocRegionFormInfoId, apDocRegionFieldId, key, values, 1, order, apDocInstFieldValueId);
            order++;
        }


        Map<String, Object> res = new HashMap<>();
        ExtJarHelper.returnValue.set(res);
    }
}
