package com.qygly.demo.ext.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author daiguanjun
 */
@Data
public class Region {
    private Integer regionType;
    private String regionTypeName;
    private String regionId;
    private Integer isFromMaster;
    private Integer order;
    private List<Field> fields;

    private List<Map<String, String >> rows;

    public Region(Object regionType, Object regionId, Object isFromMaster, Object regionTypeName, Object order) {
        this.regionType = (Integer) regionType;
        this.regionId = (String) regionId;
        this.isFromMaster = (Integer) isFromMaster;
        this.regionTypeName = (String) regionTypeName;
        this.order = (Integer) order;
    }
}
