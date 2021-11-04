package com.qygly.demo.ext.entity;

import lombok.Data;

/**
 * @author daiguanjun
 */
@Data
public class Field {
    private Object fieldName;
    private Object type;
    private Object field;
    private Object fieldValue = "";
    private Object order;
    private Object required;

    public Field(Object fieldName, Object fieldTypeName, Object fieldType, Object fieldValue, Object order, Object required) {
        this.fieldName = fieldName;
        this.type = fieldTypeName;
        this.field = fieldType;
        this.fieldValue = fieldValue;
        this.order = order;
        this.required = required;
    }

    public Field(){}
}
