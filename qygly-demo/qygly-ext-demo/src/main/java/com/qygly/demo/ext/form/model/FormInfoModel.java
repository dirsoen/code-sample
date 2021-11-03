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

    private Integer formInfoFieldCount;

    private Integer formInfoListCount;

    private Integer formInfoType;

    private List<FieldInfoModel> customFieldList;

    private Integer customFieldCount;

    private Integer customFieldListCount;

    private Integer customFieldType;

    @Override
    public String toString() {
        return "FormInfoModel{" +
                "formInfoList=" + formInfoList +
                ", formInfoFieldCount=" + formInfoFieldCount +
                ", formInfoListCount=" + formInfoListCount +
                ", customFieldList=" + customFieldList +
                ", customFieldCount=" + customFieldCount +
                ", customFieldListCount=" + customFieldListCount +
                '}';
    }
}
