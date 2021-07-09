package com.boot.smartrelay.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminDeviceModel {
    private String userId;
    private String userPw;
    private List<String> deviceId;
    private String phoneNumberOne;
    private String phoneNumberTwo;
    private String phoneNumberThree;
}
