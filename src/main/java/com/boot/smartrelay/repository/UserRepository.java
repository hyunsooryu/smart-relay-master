package com.boot.smartrelay.repository;

import com.boot.smartrelay.Utils;
import com.boot.smartrelay.beans.AdminDevice;
import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository{

    final MongoTemplate mongoTemplate;

    final AdminDeviceRepository adminDeviceRepository;


    //어드민용
    public ResponseBox createUser(User user){

        boolean status = true;
        String message = "";

        try {
            boolean existFlg = mongoTemplate.exists(Query.query(where("id").is(user.getId())), User.class, CollectionBox.USER_COLLECTION);
            if(existFlg){
                message = " 중복된 아이디가 이미 있습니다.";
                status = false;
            }else {
                mongoTemplate.insert(user);
                AdminDevice adminDevice = new AdminDevice();
                adminDevice.setId(user.getId());
                adminDevice.setDeviceIdList(null);
                mongoTemplate.insert(adminDevice);
            }
        }catch (Exception e){
            message = "몽고 DB에 새로운 유저 세이브 에러입니다.";
            status = false;
            log.error("mongoTemplate - user save error 입니다. userId : {}", user.getId());
        }
        return Utils.makeResponseBox(status,message);
    }

    //유저용
    public ResponseBox updateUserDeviceIdLists(String userId, String deviceId){
        boolean status = true;
        String message = "";
        try {
            User user = mongoTemplate.findOne(Query.query(where("id").is(userId)), User.class);
            if (user == null) {
                log.info("해당 유저가 존재하지 않습니다.");
                status = false;
                message = "해당 유저가 존재하지 않습니다.";
            }else {
                List<String> deviceIdList = user.getDeviceIdList();
                if (deviceIdList == null) {
                    deviceIdList = new ArrayList<>();
                }

                List<String> adminDeviceIdList = adminDeviceRepository.getValidDeviceIdLists(user);

                if (adminDeviceIdList == null || adminDeviceIdList.isEmpty()) {
                    log.info("해당 유저의 유효한(어드민의 등록된) 디바이스 리스트가 존재하지 않습니다.");
                    status = false;
                    message = "해당 유저의 유효한(어드민의 등록된) 디바이스 리스트가 존재하지 않습니다.";
                }else {
                    if (adminDeviceIdList.contains(deviceId)) {
                        deviceIdList.add(deviceId);
                        mongoTemplate.updateFirst(Query.query(where("id").is(userId)), Update.update("deviceIdList", deviceIdList), User.class);
                    } else {
                        log.info("해당 디바이스 아이디는 유효하지 않습니다.");
                        status = false;
                        message = "해당 디바이스 아이디는 유효하지 않습니다.";
                    }
                }
            }
        }catch (Exception e){
            status = false;
            message = "데이터 등록에 실패하였습니다.";
        }

        return Utils.makeResponseBox(status, message);
    }

    //유저용
    public User findUserById(String userId){
        User user = null;
        try {
            user = mongoTemplate.findById(userId, User.class);
        }catch (Exception e){
            log.error("mongoTemplate - user find error 입니다. userId : {}", userId);
        }
        return user;
    }

    public ResponseBox insertLargerSector(String userId, String place){

        String message = "장소 등록이 완료 되었습니다.";
            boolean status = true;

            User user = findUserById(userId);
            List<String> as_is_list = user.getLargeSector();
            if(as_is_list == null)as_is_list = new ArrayList<>();
            if(as_is_list.contains(place)){
                message = "이미 해당 장소가 등록되어 있습니다.";
                status = false;
            }else{
                as_is_list.add(place);
                try{
                    mongoTemplate.updateFirst(Query.query(where("id").is(userId)), Update.update("largeSector", as_is_list), User.class, CollectionBox.USER_COLLECTION);
                }catch (Exception e){
                    log.error("몽고 디비에 라지 섹터 인서트 에러 userId : {}", userId);
                }
        }
        return ResponseBox.builder()
                .status(status)
                .message(message)
                .build();
    }

    public ResponseBox updateUserLargeSectorList(String userId, List<String> list){
        String message = "";
        boolean status = true;
        try{
            mongoTemplate.updateFirst(Query.query(where("id").is(userId)), Update.update("largeSector", list), User.class, CollectionBox.USER_COLLECTION);
        }catch (Exception e){
            message = "DB 커넥션을 확인해주세요. 라지 섹터 변경이 되지 않았습니다. userId : " + userId;
            status = false;
        }
        return Utils.makeResponseBox(status, message);
    }

}
