package com.boot.smartrelay.service;


import com.boot.smartrelay.beans.DeviceStatus;
import com.boot.smartrelay.beans.Packet;
import com.boot.smartrelay.beans.PacketList;

import java.text.ParseException;
import java.util.List;

public interface DeviceService {
    boolean setDeviceStatus(String deviceId, List<Packet> packet);

    boolean setNewOrder(String deviceId, List<Packet> order, int channel);

    PacketList getDeviceStatus(String deviceId);

    List<Packet> getOrderIfPresent(String deviceId);

    boolean scheduledupcheck(String schedule) throws ParseException;
}
