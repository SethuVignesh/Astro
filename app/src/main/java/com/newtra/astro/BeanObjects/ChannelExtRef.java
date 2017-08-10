package com.newtra.astro.BeanObjects;

/**
 * Created by Sethu on 8/8/17.
 */

public class ChannelExtRef {
    String system;
    String subSystem;
    String value;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ChannelExtRef(String system, String subSystem, String value) {

        this.system = system;
        this.subSystem = subSystem;
        this.value = value;
    }
}
