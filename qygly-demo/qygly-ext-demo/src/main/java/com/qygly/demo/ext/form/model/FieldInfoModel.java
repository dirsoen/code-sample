package com.qygly.demo.ext.form.model;

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
    
    public FieldInfoModel(String key, String value, Boolean isMandatory, Integer order) {
        this.key = key;
        this.value = value;
        this.isMandatory = isMandatory;
        this.order = order;
    }

    private String key;

    private String value = " ";

    private Boolean isMandatory;

    private Integer order;

    @Override
    public String toString() {
        return "FieldInfoModel{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", isMandatory=" + isMandatory +
                ", order=" + order +
                '}';
    }
}
