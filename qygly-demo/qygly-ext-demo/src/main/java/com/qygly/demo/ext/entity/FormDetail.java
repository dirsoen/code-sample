package com.qygly.demo.ext.entity;

import lombok.Data;

import java.util.List;

/**
 * @author daiguanjun
 */
@Data
public class FormDetail {
    private Object id;
    private Object name;
    private Object masterId;
    private Object masterName;
    private Object type;
    private Object typeId;
    private Object isMaster;
    private Object formInstructions;
    private List<Region> regions;

    public FormDetail(Object id, Object name, Object masterId, Object masterName, Object type, Object typeId, Object isMaster, Object formInstructions) {
        this.id = id;
        this.name = name;
        this.masterId = masterId;
        this.masterName = masterName;
        this.type = type;
        this.typeId = typeId;
        this.isMaster = isMaster;
        this.formInstructions = formInstructions;
    }

    public FormDetail() {
    }
}
