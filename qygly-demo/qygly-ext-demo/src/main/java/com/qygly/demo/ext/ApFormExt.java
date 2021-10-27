package com.qygly.demo.ext;

import com.qygly.ext.jar.helper.ExtJarHelper;
import com.qygly.shared.ad.att.VarInfo;
import com.qygly.shared.ad.entity.EntityInfo;
import com.qygly.shared.ad.sev.SevInfo;
import com.qygly.shared.interaction.EntityRecord;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ApFormExt {
    public void genCode() {
        List<EntityRecord> entityRecordList = ExtJarHelper.entityRecordList.get();
        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();

        if (entityRecordList != null) {
            for (EntityRecord entityRecord : entityRecordList) {
                // 默认生成唯一码作为CODE：
//                entityRecord.valueMap = new HashMap<>();
                entityRecord.valueMap.put("CODE", UUID.randomUUID().toString());

                entityRecord.extraEditableAttCodeList = new ArrayList<>();
                entityRecord.extraEditableAttCodeList.add("CODE");

                // 获取ID：
//                String csCommId = entityRecord.csCommId;

                // 新建默认的AP_COMP：
//                String newId = ExtJarHelper.insertData("AP_COMP");
//                jdbcTemplate.update("UPDATE AP_COMP T SET T.AP_ASSET_PKG_ID=?,T.NAME='默认公司' WHERE T.ID=?",
//                        csCommId, newId);
            }
        }
    }

    public void genDefaultComp() {
        List<EntityRecord> entityRecordList = ExtJarHelper.entityRecordList.get();
        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();

        if (entityRecordList != null) {
            for (EntityRecord entityRecord : entityRecordList) {
                // 获取ID：
                String csCommId = entityRecord.csCommId;

                // 新建默认的AP_COMP：
                String newId = ExtJarHelper.insertData("AP_COMP");
                jdbcTemplate.update("UPDATE AP_COMP T SET T.AP_ASSET_PKG_ID=?,T.NAME='默认公司' WHERE T.ID=?",
                        csCommId, newId);
            }
        }
    }

    public void changeName() {
        List<EntityRecord> entityRecordList = ExtJarHelper.entityRecordList.get();
        JdbcTemplate jdbcTemplate = ExtJarHelper.jdbcTemplate.get();

        if (entityRecordList != null) {
            for (EntityRecord entityRecord : entityRecordList) {
                // 获取ID：
                String csCommId = entityRecord.csCommId;
                jdbcTemplate.update("UPDATE AP_ASSET_PKG T SET T.NAME='好的' WHERE T.ID=?",
                        csCommId);
            }
        }
    }

}
