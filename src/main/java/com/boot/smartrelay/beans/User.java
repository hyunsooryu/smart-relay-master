package com.boot.smartrelay.beans;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Builder
@AllArgsConstructor
@Slf4j
@Getter
@Setter
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String password;
    private List<String> deviceIdList;
    private boolean loginFlg;
    private List<String> largeSector;
    private List<String> smallSector;
    private String phoneNumber;
    private List<String> phoneNumberList;

    public boolean addDeviceId(String deviceId){
        if (deviceIdList == null){
            deviceIdList = new ArrayList<>();
        }
        deviceIdList.add(deviceId);
        return true;
    }
    public User(){
      this.loginFlg = false;
    };

    public int  getDeviceIdListSize(){
       return (deviceIdList == null) ? 0 : deviceIdList.size();
    }


}
