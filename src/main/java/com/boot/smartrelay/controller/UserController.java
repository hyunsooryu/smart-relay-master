package com.boot.smartrelay.controller;


import com.boot.smartrelay.beans.*;
import com.boot.smartrelay.repository.DeviceStatusMemoryRepository;
import com.boot.smartrelay.repository.UserRepository;
import com.boot.smartrelay.schedule.DeviceConditionCheckService;
import com.boot.smartrelay.service.AdminService;
import com.boot.smartrelay.service.DeviceService;
import com.boot.smartrelay.service.UserService;
import com.boot.smartrelay.validator.DeviceValidator;
import com.boot.smartrelay.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    final UserValidator userValidator;

    final DeviceValidator deviceValidator;

    final UserService userService;

    final AdminService adminService;

    final DeviceService deviceService;

    final DeviceConditionCheckService deviceConditionCheckService;

    @Resource(name="loginUser")
    private User loginUser;

    @GetMapping("/login")
    public String login(ModelMap model, @ModelAttribute("deviceUser") User deviceUser, @RequestParam(value = "fail", defaultValue = "false") String fail){
        if(loginUser.isLoginFlg()){
            return "user/login_try";
        }

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("fail", fail);
        return "user/login";
    }

    @PostMapping("/login_pro")
    public String login_pro(ModelMap model, @Valid @ModelAttribute("deviceUser") User deviceUser, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "user/login_fail";
        }
        //아이디, 비밀번호 확인
        User user = userService.findUserById(deviceUser.getId());
        if(user == null){
            return "user/login_fail";
        }
        if(user.getId().equals(deviceUser.getId()) && user.getPassword().equals(deviceUser.getPassword())) {
            loginUser.setLoginFlg(true);
            loginUser.setId(user.getId());
            return "user/login_success";
        }else{
            return "user/login_fail";
        }
    }

    @GetMapping("/not_login")
    public String not_login(){
        return "user/not_login";
    }

    @GetMapping("/logout")
    public String logout(){
        loginUser.setLoginFlg(false);
        return "user/logout";
    }

    @GetMapping("/lsec")
    public String large_sector(ModelMap model){
        User user = userService.findUserById(loginUser.getId());


        user.setLoginFlg(true);
        model.addAttribute("loginUser", user);
        return "user/large_sector";
    }


    @GetMapping("/ssec")
    public String small_sector(ModelMap model, @RequestParam("lsec") String lsec){
        //유저 정보
        User user = userService.findUserById(loginUser.getId());

        if(user.getLargeSector() == null || user.getLargeSector().isEmpty()){
            model.addAttribute("message", "잘못된 접근입니다.");
            return "noway";
        }
        boolean flg = false;
        for(String largeSector : user.getLargeSector()){
            if(largeSector.contains(lsec)){flg = true;}
        }
        if(!flg){
            model.addAttribute("message", "잘못된 접근입니다.");
            return "noway";
        }

        //세분류 선택
        List<Device> devices = userService.selectDeviceByUserIdAndLargeSector(loginUser.getId(), lsec);

        Map<String, List<String>> deviceStatueMap = new HashMap<String, List<String>>();
        //상태 데이터 동봉
        for(Device device : devices){
            List<String> tmpList = new ArrayList<>();

            if(!deviceConditionCheckService.checkIsNowAliveDevice(device.getDeviceId())){
                tmpList.add("X");
                deviceStatueMap.put(device.getSmallSector(), tmpList);
                continue;
            }

            String deviceId = device.getDeviceId();

            PacketList packetListForStatus = deviceService.getDeviceStatus(deviceId);

            if(packetListForStatus == null || packetListForStatus.getPackets() == null){
                tmpList.add("X");
                deviceStatueMap.put(device.getSmallSector(), tmpList);
                continue;
            }

            List<Packet> packets = packetListForStatus.getPackets();

            DeviceStatus deviceStatus = new DeviceStatus();
            //1. 디바이스 아이디 설정
            deviceStatus.setDeviceId(deviceId);

            //2. 마지막 커넥션 시간 갱신
           // deviceStatus.setLastSec(Instant.now().getEpochSecond());

            //3. 디바이스 패킷 중  currentState 갱신
            List<Integer> currentStates = new ArrayList<>();
            List<String> modes = new ArrayList<>();
            int sizeOfPackets = packets.size();
            for(int channel = 0; channel < sizeOfPackets; channel++){
                Packet packet = packets.get(channel);
                currentStates.add(packet.getCurrentState());
                modes.add(packet.getMode());
            }

            deviceStatus.setStatus(currentStates);
            deviceStatus.setMode(modes);

            String status = "N";
            if(deviceStatus.getStatus() != null && deviceStatus.getMode() != null) {
                for (int k = 0; k < 3; k++) {
                    if (deviceStatus.getStatus().get(k) > 0) {
                        status = "M1";
                    } else {
                        status = "M0";
                    }

                    if(!StringUtils.isEmpty(deviceStatus.getMode().get(k))){
                        String tmp = deviceStatus.getMode().get(k);
                        if(tmp.equals("a")){
                            if(deviceStatus.getStatus().get(k) > 0){
                                status = "A1";
                            }else{
                                status = "A0";
                            }
                        }else if(tmp.equals("s")){
                            if(deviceStatus.getStatus().get(k) > 0){
                                status = "S1";
                            }else{
                                status = "S0";
                            }
                        }else if(tmp.equals("r")){
                            if(deviceStatus.getStatus().get(k) > 0){
                                status = "R1";
                            }else{
                                status = "R0";
                            }
                        }
                    }

                    tmpList.add(status);
                }
            }
            Collections.reverse(tmpList);

            deviceStatueMap.put(device.getSmallSector(), tmpList);
        }

        model.addAttribute("deviceStatusMap", deviceStatueMap);

        List<String> smallSector = new ArrayList<>();
        for(Device device : devices){
            smallSector.add(device.getSmallSector());
        }


        //2. 디바이스 아이디 유효성 검사
        List<String> resultDeviceIdList = new ArrayList<>();
        List<String> alreadyDeviceIdList = new ArrayList<>();
        List<String> validDeviceIdLists = adminService.getValidDeviceIdLists(loginUser);



        //3. 이미 쓰인 디바이스 인지 여부 확인
        List<Device> alreadyInDevice = userService.selectDeviceByUserId(loginUser.getId());
        for(Device dev : alreadyInDevice){
            alreadyDeviceIdList.add(dev.getDeviceId());
        }
        for(String dev : validDeviceIdLists){
            if(alreadyDeviceIdList.contains(dev)){
                continue;
            }
            resultDeviceIdList.add(dev);
        }

        user.setDeviceIdList(resultDeviceIdList);
        user.setSmallSector(smallSector);
        model.addAttribute("targetPlace", lsec);
        user.setLoginFlg(loginUser.isLoginFlg());
        model.addAttribute("loginUser", user);
        return "user/small_sector";
    }

    @PostMapping("/lsec/add")
    public String larget_sector_add(ModelMap model, @RequestParam("place") String place){
        if(place == null || "".equals(place)){
            model.addAttribute("message", "정확한 장소값을 입력해주세요.");
            return "user/lsec_add_fail";
        }else{
            User user = userService.findUserById(loginUser.getId());
            List<String> largeSector = user.getLargeSector();
            if(largeSector.contains(place)){
                model.addAttribute("message", "이미 등록된 장소입니다.");
                return "user/lsec_add_fail";
            }
        }
        ResponseBox responseBox = userService.insertLargeSector(loginUser.getId(), place);
        if(!responseBox.isStatus()){
            model.addAttribute("message", "등록에 실패하였습니다..");
            return "user/lsec_add_fail";
        }
        model.addAttribute("message", "성공적으로 등록되었습니다.");
        return "user/lsec_add_success";
    }

    @PostMapping("/lsec/del")
    public String larget_sector_del(){
        return "user/del_success";
    }

    @PostMapping("/ssec/add")
    public String small_sector_add(ModelMap model, @Valid @ModelAttribute Device device, BindingResult bindingResult){
        if(bindingResult.hasErrors()){

            model.addAttribute("message", "정보를 정확히 입력해주세요");
            model.addAttribute("lsec", device.getLargeSector());
            return "user/ssec_add_fail";
        }
        String targetDeviceId = device.getDeviceId();
        String targetSmallSector = device.getSmallSector();



        model.addAttribute("lsec", device.getLargeSector());

        //1. 유저 아이디
        String userId = loginUser.getId();

        //2. 디바이스 아이디 유효성 검사
        List<String> list = adminService.getValidDeviceIdLists(loginUser);

        if(!list.contains(targetDeviceId)){

            model.addAttribute("message", "유효하지 않은 디바이스 키값입니다.");
            return "user/ssec_add_fail";
        }

        //3. 이미 쓰인 디바이스 인지 여부 확인 (키값, 스몰 섹터 값 모두 검사)
        List<String> smallSectorAlreadyInDeviceLargeSector = new ArrayList<>();
        List<Device> alreadyInDevice = userService.selectDeviceByUserId(userId);
        for(Device dev : alreadyInDevice){
            if(dev.getDeviceId().equals(targetDeviceId)){
                model.addAttribute("message", "이미 사용되고 있는 디바이스 키값입니다.");
                return "user/ssec_add_fail";
            }
            if(device.getLargeSector().equals(dev.getLargeSector())){
                smallSectorAlreadyInDeviceLargeSector.add(dev.getSmallSector());
            }
        }
        //4. 스몰 섹터안에 같은 이름을 가진 장소있는 지 여부 확인
        for(String dev : smallSectorAlreadyInDeviceLargeSector){
            if(dev.equals(targetSmallSector)){

                model.addAttribute("message", "이미 사용되고 있는 지명입니다.");
                return "user/ssec_add_fail";
            }
        }

        //4. 인서트
        ResponseBox responseBox = userService.insertDevice(userId, device.getLargeSector(), device.getSmallSector(), device.getDeviceId());
        if(!responseBox.isStatus()){
            model.addAttribute("message", "DB 인서트 실패입니다.");
            return "user/ssec_add_fail";
        }
        model.addAttribute(responseBox.getMessage());
        return "user/ssec_add_success";
    }

    @GetMapping("/device")
    public String device(ModelMap model, @RequestParam("smallSector") String smallSector,
                         @RequestParam("largeSector") String largeSector, @RequestParam("deviceId") String deviceId,
                         @RequestParam("no") String no, @RequestParam("nowStatus") String nowStatus){
        User user = userService.findUserById(loginUser.getId());
        List<Device> devices = userService.selectDeviceByUserId(user.getId());

        /**
         * 1. 유저 정보 및 위치 정보 설정
         */
        boolean isValidDeviceId = false;
        boolean isValidSmallSector = false;
        boolean isValidLargeSector = false;

        for(Device device : devices){
            if(device.getLargeSector() != null && device.getLargeSector().equals(largeSector)) isValidLargeSector = true;
            if(device.getSmallSector() != null && device.getSmallSector().equals(smallSector)) isValidSmallSector = true;
            if(device.getDeviceId() != null && device.getDeviceId().equals(deviceId)) isValidDeviceId = true;
            if(isValidDeviceId && isValidSmallSector && isValidLargeSector)break;
        }


        if(!isValidDeviceId || !isValidSmallSector || !isValidLargeSector){
            model.addAttribute("message", "유효한 디바이스 아이디 혹은 장소 이름이 아닙니다.");
            model.addAttribute("ls", largeSector);
            return "user/detail_fail";
        }

        boolean deviceAliveCheck = deviceConditionCheckService.checkIsNowAliveDevice(deviceId);



        if(!deviceAliveCheck){
            model.addAttribute("message", "디바이스의 전원을 확인해주세요. / 전원을 켠 1분 후부터 사용가능합니다.");
            model.addAttribute("ls", largeSector);
            return "user/detail_fail";
        }

        PacketList packetList = deviceService.getDeviceStatus(deviceId);

        if(packetList == null || packetList.getPackets() == null){
            model.addAttribute("message", "디바이스의 전원을 확인해주세요. / 전원을 켠 1분 후부터 사용가능합니다.");
            model.addAttribute("ls", largeSector);
            return "user/detail_fail";
        }

        List<Packet> recentPackets = packetList.getPackets();
        int len = recentPackets.size();
        int targetNo = -1;
        try {
            targetNo = Integer.parseInt(no) - 1;
        }catch (Exception e){

        }


        if(targetNo >=0) {
            Packet recentPacket = recentPackets.get(targetNo);
            String type = recentPacket.getMode();
            //1. autoModeLimit 설정
            model.addAttribute("autoTimeLimitVal",recentPacket.getAutoTimeLimit());
            //2. repeatMode 시간 설정 or 스케쥴 시간 설정
            if("r".equals(type)) {
                model.addAttribute("repeatModeVal", recentPacket.getSchedule());
            }
            else if("s".equals(type)) {
                model.addAttribute("scheduleModeVal", recentPacket.getSchedule());
            }
        }

        model.addAttribute("deviceId", deviceId);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("nowStatus", nowStatus);
        model.addAttribute("deviceName", no + "번 릴레이 채널");
        model.addAttribute("back", "/user/detail?smallSector=" + smallSector + "&largeSector=" + largeSector);
        model.addAttribute("no", no);

        return "user/device_sector";
    }


    @GetMapping("/detail")
    public String detail(ModelMap model, @RequestParam("smallSector") String smallSector, @RequestParam("largeSector") String largeSector){
        //검증 되어있는 스몰 섹터인지 확인이 필요합니다..
        User user = userService.findUserById(loginUser.getId());
        List<Device> devices = userService.selectDeviceByUserId(user.getId());

        /**
         * 1. 유저 정보 및 위치 정보 설정
         */
        boolean isValidDeviceId = false;
        boolean isValidSmallSector = false;
        boolean isValidLargeSector = false;

        String deviceId = "";

        for(Device device : devices){
            if(device.getLargeSector() != null && device.getLargeSector().equals(largeSector)) isValidLargeSector = true;
            if(device.getLargeSector() != null && device.getSmallSector() != null && device.getSmallSector().equals(smallSector) && device.getLargeSector().equals(largeSector)){
                deviceId = device.getDeviceId();
                isValidDeviceId = true;
                isValidSmallSector = true;
            }
            if(isValidDeviceId && isValidSmallSector && isValidLargeSector)break;
        }



        if(!isValidDeviceId || !isValidSmallSector || !isValidLargeSector){
            model.addAttribute("message", "유효한 디바이스 아이디 혹은 장소 이름이 아닙니다.");
            model.addAttribute("ls", largeSector);
            return "user/detail_fail";
        }

        boolean deviceAliveCheck = deviceConditionCheckService.checkIsNowAliveDevice(deviceId);



        if(!deviceAliveCheck){
            model.addAttribute("message", "디바이스의 전원을 확인해주세요. / 전원을 켠 1분 후부터 사용가능합니다.");
            model.addAttribute("ls", largeSector);
            return "user/detail_fail";
        }

        /**
         * 2. 디바이스 정보 설정
         */
        PacketList packetListForStatus = deviceService.getDeviceStatus(deviceId);

        List<Packet> packets = packetListForStatus.getPackets();

        if(packetListForStatus == null || packetListForStatus.getPackets() == null){
            model.addAttribute("message", "디바이스의 전원을 확인해주세요. / 전원을 켠 1분 후부터 사용가능합니다.");
            model.addAttribute("ls", largeSector);
            return "user/detail_fail";
        }


        DeviceStatus deviceStatus = new DeviceStatus();
        //1. 디바이스 아이디 설정
        deviceStatus.setDeviceId(deviceId);

        //2. 마지막 커넥션 시간 갱신
        //deviceStatus.setLastSec(Instant.now().getEpochSecond());

        //3. 디바이스 패킷 중  currentState 갱신
        List<Integer> currentStates = new ArrayList<>();
        List<String> modes = new ArrayList<>();
        int sizeOfPackets = packets.size();
        for(int channel = 0; channel < sizeOfPackets; channel++){
            Packet packet = packets.get(channel);
            currentStates.add(packet.getCurrentState());
            modes.add(packet.getMode());
        }

        deviceStatus.setStatus(currentStates);
        deviceStatus.setMode(modes);


        for(int k = 0; k < 3; k++){
            model.addAttribute("nowStatus" + k, (k + 1) + "번 릴레이 채널 정보없음");
            model.addAttribute("nowStatusMini" + k, "상태정보 없음");
        }

        if(deviceStatus != null){
            List<Integer> deviceState = deviceStatus.getStatus();
            List<String> deviceMode = deviceStatus.getMode();

            if(deviceState != null && deviceMode != null) {
                for (int channel = 0; channel < Math.min(deviceState.size(), deviceMode.size()); channel++) {
                    String nowStatus = (channel + 1) + "번 릴레이 채널(";
                    if (deviceState.get(channel) > 0) {
                        String mode = deviceMode.get(channel);
                        if ("s".equals(mode)) {
                            nowStatus += "스케쥴모드 작동중";
                        } else if ("a".equals(mode)) {
                            nowStatus += "오토모드 작동중";
                        } else if ("r".equals(mode)) {
                            nowStatus += "반복모드 작동중";
                        } else{
                            nowStatus += "켜져있음";
                        }

                    } else {
                        nowStatus += "꺼져있음";
                    }

                    model.addAttribute("nowStatusMini" + channel, nowStatus.split("채널")[1] + ")");
                    nowStatus += ")";

                    model.addAttribute("nowStatus" + channel, nowStatus);
                }
            }
        }


        /**
         * 3. 최종 렌더링 데이타 설정
         */

        model.addAttribute("deviceId", deviceId);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("place", smallSector);
        model.addAttribute("largeSector", largeSector);
        model.addAttribute("back", "/user/ssec?lsec=" + largeSector);
        String s1 = (String)model.getAttribute("nowStatusMini0");
        String s2 = (String)model.getAttribute("nowStatusMini1");
        String s3 = (String)model.getAttribute("nowStatusMini2");
        String firstUrl = "/user/device?no=1&deviceId=" + deviceId + "&smallSector=" + smallSector + "&largeSector=" + largeSector + "&nowStatus=" + s1;
        String secondUrl = "/user/device?no=2&deviceId=" + deviceId + "&smallSector=" + smallSector + "&largeSector=" + largeSector + "&nowStatus=" + s2;
        String thirdUrl = "/user/device?no=3&deviceId=" + deviceId + "&smallSector=" + smallSector + "&largeSector=" + largeSector + "&nowStatus=" + s3;
        model.addAttribute("firstUrl", firstUrl);
        model.addAttribute("secondUrl", secondUrl);
        model.addAttribute("thirdUrl", thirdUrl);
        return "user/detail_sector";
    }




    @PostMapping("/ssec/del")
    public String target_sector_del(){
        return "user/del_success";
    }

    @InitBinder("deviceUser")
    void initBinder1(WebDataBinder webDataBinder){
        webDataBinder.addValidators(userValidator);
    }

    @InitBinder("userDevice")
    void initBinder2(WebDataBinder webDataBinder){
        webDataBinder.addValidators(deviceValidator);
    }

}
