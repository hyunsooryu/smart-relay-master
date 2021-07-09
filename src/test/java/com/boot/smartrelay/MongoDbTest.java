package com.boot.smartrelay;

import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.User;
import com.boot.smartrelay.repository.CollectionBox;
import com.boot.smartrelay.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@SpringBootTest
public class MongoDbTest {

    @Autowired
    AdminService adminService;
    @Autowired
    MongoTemplate mongoTemplate;


    @Test
    @DisplayName("유저 생성 및 유저-다비아스 맵핑 어드민 정보 생성")
    void createUserTest(){
        User user = User.builder()
                .loginFlg(false)
                .password("1234")
                .id("test-user-1")
                .largeSector(new ArrayList<>())
                .smallSector(new ArrayList<>())
                .deviceIdList(new ArrayList<>())
                .build();

        ResponseBox responseBox1;
        if(true){
            responseBox1 = adminService.addDeviceToUser("test-user-1", Arrays.asList(new String[]{"device-1","device-2","device-3"}));
        }

        //System.out.println(responseBox.getMessage());
       // System.out.println(responseBox.isStatus());
        System.out.println(responseBox1.getMessage());
        System.out.println(responseBox1.isStatus());
    }

    @Test
    void mongoInsertTest(){
        String userId = "user-1";
        List<String> list = new ArrayList<>();
        list.add("광화문");
        try {
            User user = mongoTemplate.findById("user-1", User.class, CollectionBox.USER_COLLECTION);
            System.out.println(user);
            mongoTemplate.updateFirst(Query.query(where("id").is("user-1")), Update.update("largeSector", list), User.class, CollectionBox.USER_COLLECTION);
        }catch(Exception e){
            System.out.println("Error");
        }
    }
}
