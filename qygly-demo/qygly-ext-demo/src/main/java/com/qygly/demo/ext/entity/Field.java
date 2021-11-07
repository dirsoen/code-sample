package com.qygly.demo.ext.entity;

import lombok.Data;
import org.springframework.util.Assert;

/**
 * @author daiguanjun
 */
@Data
public class Field {
    private String fieldId;
    private String fieldName;
    private String fieldType;
    private String fieldCode;
    private Integer isMandatory;
    private Integer order;

    public Field(Object fieldId, Object fieldName, Object fieldType, Object fieldCode, Object isMandatory, Object order) {
        this.fieldId = (String) fieldId;
        this.fieldName = (String) fieldName;
        this.fieldType = (String) fieldType;
        this.fieldCode = (String) fieldCode;
        this.isMandatory = (Integer) isMandatory;
        this.order = (Integer) order;
    }
}
