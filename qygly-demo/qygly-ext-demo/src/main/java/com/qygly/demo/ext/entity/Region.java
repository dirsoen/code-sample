package com.qygly.demo.ext.entity;

import lombok.Data;

import java.util.List;

/**
 * @author daiguanjun
 */
@Data
public class Region {
    private Object regionType;
    private Object order;
    private Object regionId;
    private Object regionTypeId;
    private Object isFromMaster;
    private List<Field> fields;

    public Region(Object regionType, Object order, Object regionId, Object regionTypeId, Object isFromMaster) {
        this.regionType = regionType;
        this.order = order;
        this.regionId = regionId;
        this.regionTypeId = regionTypeId;
    }

    public Region() {
    }
}
