package com.qygly.demo.ext.entity;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xuzhifeng
 * @Date 2021/10/26 3:10 下午
 */
@Data
public class FormInfoModel {
    private List<FieldInfoModel> fields;

    @NotNull(message = "区域类型不能为空")
    private Integer regionType;

    @NotNull(message = "区域排序不能为空")
    private Integer order;

    @NotNull(message = "是否来自母版不能为空")
    private Integer isFromMaster;

}
