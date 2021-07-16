package com.boot.smartrelay.schedule;

import com.boot.smartrelay.beans.DeviceStatus;
import com.boot.smartrelay.beans.PacketList;
import com.boot.smartrelay.repository.CollectionBox;
import com.boot.smartrelay.repository.DeviceStatusMemoryRepository;
import com.boot.smartrelay.repository.DeviceStatusRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeviceConditionCheckServiceImpl implements DeviceConditionCheckService {

    final DeviceStatusMemoryRepository deviceStatusMemoryRepository;

    final MongoTemplate mongoTemplate;

    final ConcurrentMap<String, Boolean> ON_DEVICE_MAP;

    final SmartAligoApiService smartAligoApiService;

    @Scheduled(fixedRate = 60000)
    @Override
    public void deviceServiceCheck(){
        log.info("device service check");
        List<List<String>> status = deviceStatusMemoryRepository.getDevicesOnOrOff();
        List<String> on = status.get(0);
        List<String> off = status.get(1);

        boolean testApi = true;

        for(String deviceId : on){
            boolean isAlreadyOn = ON_DEVICE_MAP.getOrDefault(deviceId, false);
            if(isAlreadyOn){

            }else{ //이전에 OFF인데 지금 신호가 잡힌경우
                    if(testApi) {
                        boolean flg = smartAligoApiService.sendYesSignalMessage(deviceId);
                        if (flg)
                            log.info("deviceId : " + deviceId + " 서비스 신호 잡힘 - 시작");
                    }
            }
            ON_DEVICE_MAP.put(deviceId, true);
        }

        for(String deviceId : off){
            boolean isAlreadyOn = ON_DEVICE_MAP.getOrDefault(deviceId, false);
            if(isAlreadyOn){ //이전에 ON인데 지금 신호가 안잡힌 경우
                    if(testApi) {
                        boolean flg = smartAligoApiService.sendNoSignalMessage(deviceId);
                        if (flg) {
                            log.info("deviceId : " + deviceId + "서비스 신호 누락 - 확인요망");
                        }
                    }

            }
            ON_DEVICE_MAP.put(deviceId, false);
        }
    }

    public boolean checkIsNowAliveDevice(String deviceId){
        boolean flg =  ON_DEVICE_MAP.containsKey(deviceId);
        if(!flg)return false;
        flg = ON_DEVICE_MAP.getOrDefault(deviceId, false);
        return flg;
    }


}
