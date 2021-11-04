package com.qygly.demo.ext.entity;

import lombok.Data;
import org.springframework.util.Assert;

/**
 * @author xuzhifeng
 * @Date 2021/10/26 3:12 下午
 */
@Data
public class FieldInfoModel {
    public FieldInfoModel() {
    }

    public FieldInfoModel(String fieldId, String value, Integer isMandatory, Integer order) {
        this.fieldId = fieldId;
        this.value = value;
        this.isMandatory = isMandatory;
        this.order = order;
    }

    private String fieldId;

    private String value = " ";

    private Integer isMandatory;

    private Integer order;

    private String emptyString = "";

}
