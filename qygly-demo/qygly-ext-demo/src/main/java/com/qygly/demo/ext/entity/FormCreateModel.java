package com.qygly.demo.ext.entity;

import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author xuzhifeng
 * @Date 2021/10/26 3:09 下午
 */
@Data
public class FormCreateModel {

    private String id;

    private String formNo;

    private String formName;

    private String formType;

    private String masterMask;

    private String formInstructions;

    private List<FormInfoModel> regions;

}
