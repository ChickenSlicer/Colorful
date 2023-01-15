package com.hyg.domain;

import com.hyg.enums.ServiceType;

public class TaskInfo {
    private String param;
    private ServiceType serviceType;

    public TaskInfo(String param, ServiceType serviceType) {
        this.param = param;
        this.serviceType = serviceType;
    }

    public TaskInfo() {
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "param='" + param + '\'' +
                ", serviceType=" + serviceType +
                '}';
    }
}
