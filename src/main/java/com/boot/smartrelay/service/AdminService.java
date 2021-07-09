package com.boot.smartrelay.service;

import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.User;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface AdminService {

    ResponseBox createUser(User user);

    ResponseBox addDeviceToUser(String userId, List<String> deviceId);

    ResponseBox deleteDeviceFromUser(String userId, List<String> deviceId);

    List<String> getValidDeviceIdLists(User user);
    
    boolean isExistDeviceIds(List<String> deviceIds);
    
    ResponseBox saveSmartRelayDeviceIds(List<String> deviceIds);

}
