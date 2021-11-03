package com.qygly.demo.ext.form.model;

import lombok.Data;
import java.util.List;

/**
 * @author xuzhifeng
 * @Date 2021/10/26 3:10 下午
 */
@Data
public class FormInfoModel {
    private List<FieldInfoModel> formInfoList;

    private List<FieldInfoModel> customFieldList;

    @Override
    public String toString() {
        return "FormInfoModel{" +
                "formInfoList=" + formInfoList +
                ", customFieldList=" + customFieldList +
                '}';
    }
}
