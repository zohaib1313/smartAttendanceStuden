package com.zohaib.smartattendancestudent.models;

public class ModelStudents {

    private String rollNo, name, deviceId;

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ModelStudents(String rollNo, String name, String deviceId) {
        this.rollNo = rollNo;
        this.name = name;
        this.deviceId = deviceId;
    }
}
