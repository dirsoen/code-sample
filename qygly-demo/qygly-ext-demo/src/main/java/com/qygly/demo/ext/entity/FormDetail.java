package com.qygly.demo.ext.entity;

import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author daiguanjun
 */
@Data
public class FormDetail {
    private String formId;
    private String formName;
    private String formCode;
    private String masterId;
    private String masterName;
    private Integer formType;
    private String typeName;
    private Integer isMaster;
    private String remark;

    private List<Region> regions;

    public FormDetail(Object formId, Object formName, Object formCode, Object masterId,
                      Object masterName, Object formType, Object typeName, Object isMaster, Object remark) {
        this.formId = (String) formId;
        this.formName = (String) formName;
        this.formCode = (String) formCode;
        this.masterId = (String) masterId;
        this.masterName = (String) masterName;
        this.formType = Integer.parseInt((String) formType);
        this.typeName = (String) typeName;
        this.isMaster = (Integer) isMaster;
        this.remark = (String) remark;
    }
}
