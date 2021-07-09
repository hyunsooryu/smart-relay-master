package com.boot.smartrelay.schedule;

public interface DeviceConditionCheckService {
    void deviceServiceCheck();

    boolean checkIsNowAliveDevice(String deviceId);
}
