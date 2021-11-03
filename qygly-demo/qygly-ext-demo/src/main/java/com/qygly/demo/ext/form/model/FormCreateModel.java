package com.qygly.demo.ext.form.model;

import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author xuzhifeng
 * @Date 2021/10/26 3:09 下午
 */
@Data
public class FormCreateModel {
    private String formNo;

    private String formName;

    private String formType;

    private String masterMask;

    private String formInstructions;

    private FormInfoModel formInfo;

    @Override
    public String toString() {
        return "FormCreateModel{" +
                "formNo='" + formNo + '\'' +
                ", formName='" + formName + '\'' +
                ", formType='" + formType + '\'' +
                ", masterMask='" + masterMask + '\'' +
                ", formInstructions='" + formInstructions + '\'' +
                ", formInfo=" + formInfo +
                '}';
    }
}
