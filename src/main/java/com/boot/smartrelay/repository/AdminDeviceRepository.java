package com.boot.smartrelay.repository;

import com.boot.smartrelay.Utils;
import com.boot.smartrelay.beans.AdminDevice;
import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.SmartRelayDevice;
import com.boot.smartrelay.beans.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminDeviceRepository {

    final MongoTemplate mongoTemplate;

    public ResponseBox updateAdminUserDeviceIdLists(String userId, List<String> deviceId){
        boolean status = true;
        String message = "";
        try {
            AdminDevice adminDevice = mongoTemplate.findById(userId, AdminDevice.class, CollectionBox.ADMIN_BOX_COLLECCTION);
            if (adminDevice == null) {
                status = false;
                message = "해당 유저가 존재하지 않습니다. 유저 생성 먼저 해주세요.";
            } else {
                List<String> deviceIdList = adminDevice.getDeviceIdList();
                if (deviceIdList == null) {
                    deviceIdList = new ArrayList<>();
                }
                for(String device : deviceId){
                    if(!device.equals("") && !deviceIdList.contains(device)){
                        deviceIdList.add(device);
                    }
                }
                mongoTemplate.updateFirst(Query.query(where("id").is(userId)), Update.update("deviceIdList", deviceIdList), AdminDevice.class, CollectionBox.ADMIN_BOX_COLLECCTION);
            }
            }catch(Exception e){
                status = false;
                message = "저장에 실패하였습니다. DB 커넥션을 확인해주시기 바랍니다.";
                log.error("mongoTemplate - user update error 입니다. userId : {}", userId);
            }

        return Utils.makeResponseBox(status, message);
    }

    public ResponseBox modifyAdminUserDeviceIdLists(String userId, List<String> deviceId){
        boolean status = true;
        String message = "성공적으로 수정되었습니다.";

        try{
            List<String> to_be_list = new ArrayList<>();
            AdminDevice adminDevice = mongoTemplate.findById(userId, AdminDevice.class, CollectionBox.ADMIN_BOX_COLLECCTION);
            if(adminDevice == null){
                status = false;
                message = "해당 유저가 존재하지 않습니다. 유저 생성 먼저 해주세요.";
            }else{
               List<String> as_is_list = adminDevice.getDeviceIdList();
               if(as_is_list == null){
                   status = false;
                   message = "해당 유저에게 할당 된 디바이스가 존재하지 않습니다.";
               }else{
                   boolean modifyFlg = false;
                   for(String device : as_is_list){
                       if(deviceId.contains(device)){
                            modifyFlg = true;
                       }else{
                            to_be_list.add(device);
                       }
                   }
                   if(!modifyFlg){
                      status = false;
                      message = "해당 유저에게 할당 된 디바이스가 아니여서 삭제할 수 없습니다.";
                   }else {
                       mongoTemplate.updateFirst(Query.query(where("id").is(userId)), Update.update("deviceIdList", to_be_list), AdminDevice.class, CollectionBox.ADMIN_BOX_COLLECCTION);
                   }
               }
            }
        }catch(Exception e){
            status = false;
            message = "수정에 실패하였습니다. DB 커넥션을 확인해주시기 바랍니다.";
            log.error("mongoTemplate - modifyAdminUserDeviceIdLists error 입니다. userId : {}", userId);
        }
        return ResponseBox.builder()
                .message(message)
                .status(status)
                .build();
    }

    public List<String> getValidDeviceIdLists(User user){
        String userId = user.getId();
        List<String> adminDeviceIdLists = null;
        try{
            AdminDevice adminDevice = mongoTemplate.findById(userId, AdminDevice.class, CollectionBox.ADMIN_BOX_COLLECCTION);
            adminDeviceIdLists = adminDevice.getDeviceIdList();
        }catch (Exception e){
            log.error("유효하고 추가가능한 디바이스를 리턴하는데 실패 에러 user : {}", user.getId());
        }
        return adminDeviceIdLists;
    }
    
    public boolean isExistDeviceIds(List<String> deviceIds) {
    	boolean result = false;
		try {
			result = mongoTemplate.exists(Query.query(where("deviceId").in(deviceIds)), SmartRelayDevice.class, "smartRelayDevice");
		}catch(Exception e) {
			log.error("server mongo error : {}", deviceIds.toString() + "의 중복을 조회 중 에러발생");
		}
    	return result;
    }
    
    public ResponseBox saveDeviceIds(List<String> deviceIds) {
    	boolean status = true;
    	String message = "";
		try {
			for(String deviceId : deviceIds) {
				SmartRelayDevice smartRelayDevice = new SmartRelayDevice();
				smartRelayDevice.setDeviceId(deviceId);
				mongoTemplate.save(smartRelayDevice, "smartRelayDevice");
			}
		}catch(Exception e) {
			status = false;
			message = "server mongo error : 몽고 디비에 디바이스 아이디를 저장하는 데 실패했습니다.";
			log.error("server mongo error : {}", deviceIds.toString() + "을 저장하는 중 에러발생");
		}
    	return Utils.makeResponseBox(status, message);
    }

}
