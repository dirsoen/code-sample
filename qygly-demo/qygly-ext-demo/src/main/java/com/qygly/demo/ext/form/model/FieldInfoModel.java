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
    
    public FieldInfoModel(String key, String value, Integer isMandatory, Integer order, Integer regionOrder) {
        this.key = key;
        this.value = value;
        this.isMandatory = isMandatory;
        this.order = order;
        this.regionOrder = regionOrder;
    }

    private String key;

    private String value = " ";

    private Integer isMandatory;

    private Integer order;
    //区域排序 单行-单数 多行-偶数
    private Integer regionOrder;

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
