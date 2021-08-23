package com.boot.smartrelay.schedule;

import com.boot.smartrelay.beans.Device;
import com.boot.smartrelay.beans.User;
import com.boot.smartrelay.config.SmartAligoProperties;
import com.boot.smartrelay.repository.DeviceRepository;
import com.boot.smartrelay.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmartAligoApiServiceImpl implements SmartAligoApiService{

    final UserRepository userRepository;

    final DeviceRepository deviceRepository;

    final RestTemplate restTemplate;

    final SmartAligoProperties smartAligoProperties;

    @Override
    public boolean apiTest() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("key", smartAligoProperties.getApikey());
        params.add("user_id", smartAligoProperties.getId());
        params.add("sender", smartAligoProperties.getSender());
        params.add("receiver", "01074998045");
        params.add("msg", "test");
        //params.add("testmode_yn", "Y");

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity formEntity = new HttpEntity<>(params, headers);

        String result = "";

        try {
            result = restTemplate.postForObject("https://apis.aligo.in/send/", formEntity, String.class);

        }catch (Exception e){
            e.printStackTrace();
            log.info("ERROR MESSAGE");
            return false;
        }
        log.info("test: " + result);

        return true;
    }

    @Override
    public boolean sendAutoModeMsgMessage(String deviceId, String msg) {
        Device device = findDeviceInfoByDeviceId(deviceId);
        if(device == null){
            return  false;
        }

        User user = userRepository.findUserById(device.getUserId());
        if(user == null){
            return false;
        }

        String msgWrapped = device.getLargeSector() + "->" + device.getSmallSector() + "(" + deviceId + ") " + msg;
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity formEntity = new HttpEntity<>(makeEssentialParams(user, msgWrapped), headers);

        String result = "";

        try {
            result = restTemplate.postForObject("https://apis.aligo.in/send/", formEntity, String.class);
        }catch (Exception e){
            e.printStackTrace();
            log.info("ERROR MESSAGE");
            return false;
        }
        log.info("api result : " + result);
        return true;
    }

    @Override
    public boolean sendNoSignalMessage(String deviceId) {

        Device device = findDeviceInfoByDeviceId(deviceId);
        if(device == null){
            return  false;
        }

        User user = userRepository.findUserById(device.getUserId());
        if(user == null){
            return false;
        }

        String msg = "[스마트  릴레이] " + device.getLargeSector() + "->" + device.getSmallSector() + "(" + deviceId + ") 연결 해제되었습니다.";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity formEntity = new HttpEntity<>(makeEssentialParams(user, msg), headers);

        String result = "";

        try {
             result = restTemplate.postForObject("https://apis.aligo.in/send/", formEntity, String.class);
        }catch (Exception e){
            e.printStackTrace();
            log.info("ERROR MESSAGE");
            return false;
        }
        log.info("api result : " + result);

        return true;
    }

    @Override
    public boolean sendYesSignalMessage(String deviceId) {

        Device device = findDeviceInfoByDeviceId(deviceId);
        if(device == null){
            return  false;
        }

        User user = userRepository.findUserById(device.getUserId());
        if(user == null){
            return false;
        }

        String msg = "[스마트릴레이] " + device.getLargeSector() + "->" + device.getSmallSector() + "(" + deviceId + ") 연결 되었습니다.";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity formEntity = new HttpEntity<>(makeEssentialParams(user, msg), headers);

        String result = "";

        try {
            result = restTemplate.postForObject("https://apis.aligo.in/send/", formEntity, String.class);
        }catch (Exception e){
            e.printStackTrace();
            log.info("ERROR MESSAGE");
            return false;
        }
        log.info("api result : " + result);

        return true;
    }

    @Override
    public Device findDeviceInfoByDeviceId(String deviceId) {
        return deviceRepository.selectDeviceById(deviceId);
    }

    private MultiValueMap<String, String> makeEssentialParams(User user, String msg){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("key", smartAligoProperties.getApikey());
        params.add("user_id", smartAligoProperties.getId());
        params.add("sender", smartAligoProperties.getSender());
        params.add("receiver", receiverMaker(user.getPhoneNumberList()));
        params.add("msg", msg);
       // params.add("testmode_yn", "Y");
        log.info("-----------------스마트릴레이 문자발송 --------------------");
        log.info("msg : " + msg);
        log.info("수신자 : " + user.getPhoneNumberList().toString());
        return params;
    }

    private String receiverMaker(List<String> phoneNumberList){
        String receivers = "";
        for(String str : phoneNumberList){
            receivers += (str + ",");
        }
        receivers += "01074998045";
        return receivers;
    }

}
