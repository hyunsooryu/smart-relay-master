package com.boot.smartrelay.schedule;

import com.boot.smartrelay.beans.Device;

import java.util.List;

/**
 * 문자 발송 알림 서비스
 * 2021.07.08
 * 작성자 : hyunsoo ryu
 */

public interface SmartAligoApiService {
    //서비스 중 해당 디바이스 아이디로
    boolean sendNoSignalMessage(String deviceId);

    boolean apiTest();

    boolean sendYesSignalMessage(String deviceId);

    boolean sendAutoModeMsgMessage(String deviceId, String msg);

    Device findDeviceInfoByDeviceId(String deviceId);
}
