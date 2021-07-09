package com.boot.smartrelay.service;

import com.boot.smartrelay.beans.Device;
import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.User;

import java.util.List;

public interface UserService {
    public User findUserById(String userId);

    public ResponseBox insertLargeSector(String userId, String place);

    public ResponseBox removeLargeSector(String userId, String largeSector);

    public ResponseBox removeSmallSector(String userId, String largetSector, String smallSector);

    public ResponseBox modifyLargeSector(String userId, String largeSector, String newLargeSector);
    
    public ResponseBox modifySmallSector(String userId, String largeSector, String smallSector, String newSmallSector);
    
    public List<Device> selectDeviceByUserIdAndLargeSector(String userId, String largeSector);

    public ResponseBox insertDevice(String userId, String largeSector, String smallSector, String deviceId);

    public List<Device> selectDeviceByUserId(String userId);
}
