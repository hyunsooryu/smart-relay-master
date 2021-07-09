package com.boot.smartrelay;

import com.boot.smartrelay.beans.Device;
import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.beans.User;
import com.boot.smartrelay.repository.AdminDeviceRepository;
import com.boot.smartrelay.repository.DeviceRepository;
import com.boot.smartrelay.repository.UserRepository;
import com.boot.smartrelay.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MongoDbTest2 {

    @Autowired
    AdminDeviceRepository adminDeviceRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;



    @Test
    @DisplayName("유효한 디바이스 리턴하는 페이지")
    void validDeviceReturn() {
        User user = new User();
        user.setId("user-1");
        List<String> validFromAdminDevices = adminDeviceRepository.getValidDeviceIdLists(user);
        List<String> result = new ArrayList<>();
        List<Device> devices = deviceRepository.selectDeviceByUserId(user.getId());
        List<String> alreadyRegisteredDevices = new ArrayList<>();
        for (Device device : devices) {
            alreadyRegisteredDevices.add(device.getDeviceId());
        }
        for (String device : validFromAdminDevices) {
            if (!alreadyRegisteredDevices.contains(device)) {
                result.add(device);
            }

            System.out.println(result.toString());
        }
    }

    @Test
    @DisplayName("스몰 섹터 추가")
    void smallSectorAdded(){
     ResponseBox responseBox =  userService.insertDevice("user-1", "광화문", "2층", "device-2");
     System.out.println(responseBox.getMessage());
    }

    @Test
    @DisplayName("스몰 섹터 장소 이름 중복 확인")
    void isAlreadyInSmallSector(){
        ResponseBox responseBox = deviceRepository.removeLargeSector("user-1", "광화문");
        System.out.println(responseBox.getMessage());
        System.out.println(responseBox.isStatus());

    }

    @Test
    @DisplayName("라지섹터 삭제 테스트")
    void largeSectorDelTest(){
        userService.removeLargeSector("user-1", "숭실대학교");
    }

    @Test
    @DisplayName("스몰섹터 삭제 테스트")
    void smallSectorDelTest(){
        userService.removeSmallSector("user-1", "광화문", "이순신 동상 앞");
    }




}

