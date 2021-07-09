package com.boot.smartrelay.repository;

import com.boot.smartrelay.Utils;
import com.boot.smartrelay.beans.AdminDevice;
import com.boot.smartrelay.beans.Device;
import com.boot.smartrelay.beans.ResponseBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DeviceRepository {
    final MongoTemplate mongoTemplate;

    public ResponseBox insertDevice(Device device){
        boolean status = true;
        String message = "디바이스 정보 입력을 완료했습니다.";
        try {
          Device saved =  mongoTemplate.insert(device, "userDevice");
        }catch (Exception e){
            log.error("device saved error deviceId : {}", device.getDeviceId());
            status = false;
            message = "몽고 디비 커넥션을 확인하세요. 디바이스 정보 입력을 실패했습니다.";
        }
        return Utils.makeResponseBox(status, message);
    }

    public Device selectDeviceById(String deviceId){
        Device device = null;
        try{
            device = mongoTemplate.findById(deviceId, Device.class, CollectionBox.USER_DEVICE_COLLECTION);
        }catch (Exception e){
            log.error("device select error deviceId : {}", deviceId);
        }
        return device;
    }

    public List<Device> selectDeviceByUserId(String userId){
        List<Device> devices = null;
        try{
            devices = mongoTemplate.find(Query.query(where("userId").is(userId)), Device.class ,CollectionBox.USER_DEVICE_COLLECTION);
        }catch(Exception e){
            log.error("devices select error userId : {}", userId);
        }
        return devices;
    }

    public List<Device> selectDeviceByUserIdAndLargeSector(String userId, String largeSector){
        List<Device> devices = null;
        try{
            devices = mongoTemplate.find(Query.query(where("userId").is(userId).and("largeSector").is(largeSector)),
                    Device.class, CollectionBox.USER_DEVICE_COLLECTION);
        }catch(Exception e){
            log.error("devices by userId and Lsec select error userId : {}", userId);
        }
        return devices;
    }

    public ResponseBox removeLargeSector(String userId, String largeSector){
        String message = "성공적으로 라지섹터를 삭제하였습니다.";
        boolean status = true;
        try{
            var result = mongoTemplate.remove(Query.query(where("largeSector").is(largeSector).and("userId").is(userId)), Device.class, CollectionBox.USER_DEVICE_COLLECTION);
        }catch (Exception e){
            log.error("device largeSector remove error");
            message = "디비 커넥션을 확인해주세요";
            status = false;

        }
        return Utils.makeResponseBox(status, message);
    }

    public ResponseBox removeSmallSector(String userId, String largeSector, String smallSector){
        String message = "성공적으로 스몰섹터를 삭제하였습니다.";
        boolean status = true;
        try{
            var result = mongoTemplate.remove(Query.query(where("smallSector").is(smallSector)
                    .and("largeSector").is(largeSector)
                    .and("userId").is(userId)), Device.class, CollectionBox.USER_DEVICE_COLLECTION);
        }catch (Exception e){
            log.error("device smallSector remove error");
            message = "디비 커넥션을 확인해주세요";
            status = false;
        }
        return Utils.makeResponseBox(status, message);
    }
    
   public ResponseBox modifyLargeSector(String userId, String largeSector, String newLargeSector) {
		
    	String message = "성공적으로 라지섹터를 수정하였습니다.";
    	boolean status = true;
    	
    	try {
    		var result = mongoTemplate.updateMulti(Query.query(where("userId").is(userId)
    				.and("largeSector").is(largeSector)),
    				Update.update("largeSector", newLargeSector),
    				Device.class,
    				CollectionBox.USER_DEVICE_COLLECTION
    				);
    	}catch(Exception e) {
    		log.error("device largeSector modify error");
    		message = "디비 커넥션을 확인해주세요";
    		status = false;
    	}
    
    	return Utils.makeResponseBox(status, message);
    }
    
    public ResponseBox modifySmallSector(String userId, String largeSector, String smallSector, String newSmallSector) {
    	String message = "성공적으로 스몰섹터를 수정하였습니다.";
    	boolean status = true;
    	
    	try {
    		var result = mongoTemplate.updateFirst(Query.query(where("userId").is(userId)
    				.and("largeSector").is(largeSector)
    				.and("smallSector").is(smallSector)
    				),
    				Update.update("smallSector",  newSmallSector),
    				Device.class,
    				CollectionBox.USER_DEVICE_COLLECTION
    				);
    	}catch(Exception e) {
    		log.error("device smallSector modify error");
    		message = "디비 커넥션을 확인해주세요";
    		status = false;
    	}
    
    	return Utils.makeResponseBox(status, message);
    }
   
}
