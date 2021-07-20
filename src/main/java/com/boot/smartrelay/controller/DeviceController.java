package com.boot.smartrelay.controller;
import com.boot.smartrelay.beans.DeviceStatus;
import com.boot.smartrelay.beans.Packet;
import com.boot.smartrelay.beans.PacketList;
import com.boot.smartrelay.beans.PacketWrapper;
import com.boot.smartrelay.schedule.SmartAligoApiService;
import com.boot.smartrelay.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequestMapping("/device")
@RequiredArgsConstructor
@Controller
public class DeviceController {

    private final DeviceService deviceService;

    private final SmartAligoApiService smartAligoApiService;

    @PostMapping(value = "/order/{deviceId}", consumes = "application/json", produces = "application/json")
    @ResponseBody
    List<Packet> connectionWithDevice(@PathVariable("deviceId") String deviceId, @RequestBody List<Packet> packets){

        log.info("packet 도착 - 커넥션 확인 with deviceId : " + deviceId);
        //1. autoModeMsg 확인
        String autoModeMsg = checkPacketHasAutoModeMsg(packets);

        //2. status 설정
        deviceService.setDeviceStatus(deviceId , packets);
        //3. if 새로운 order 가 있다면 get

        List<Packet> result = deviceService.getOrderIfPresent(deviceId);
        if(result.size() == 0){
            log.info("{} 디바이스 : 세팅되어 있는 오더가 없습니다.", deviceId);
        }
        if(result.size() > 0){
            log.info("{} 디바이스 세팅되어 있는 오더 : {}" ,deviceId, result.toString());
        }


        if(!autoModeMsg.equals("")){
            boolean smartApiResult = smartAligoApiService.sendAutoModeMsgMessage(deviceId, autoModeMsg);
            if(!smartApiResult){
                log.info("deviceId : {}, 오토 모드 메시지 발신 실패", deviceId);
            }
        }

        return result;
    }

    @GetMapping(value = "/status/{deviceId}")
    @ResponseBody
    PacketList getPacketListForStatus(@PathVariable("deviceId") String deviceId){
        return deviceService.getDeviceStatus(deviceId);
    }


    @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String setOrder(@RequestBody PacketList packetList){
        boolean result = deviceService.setNewOrder(packetList.getDeviceId(), packetList.getPackets(), 1);
        if(result){
            return "Y";
        }
        return "N";
    }

    @PostMapping(value = "/user/scheduleMode")
    public String setScheduledOrder(HttpServletRequest request, ModelMap model, @RequestParam("deviceId") String deviceId, @RequestParam("channel") int channel, @ModelAttribute Packet packet){

        String referer = request.getHeader("referer");
        model.addAttribute("referer", referer);

        if(channel <= 0 || !StringUtils.hasLength(deviceId)){
            model.addAttribute("message", "스케쥴 값을 정확히 입력해주세요");
            //TODO 예외처리 하기
            return "device/error";
        }


        boolean scheduleValidation = false;
        try {
            String emptySchedule = "0/0/0/0/0/0/0";
            if(emptySchedule.equals(packet.getSchedule())){
                model.addAttribute("message", "스케쥴 값을 정확히 입력해주세요");
                return "device/error";
            }
            scheduleValidation = deviceService.scheduledupcheck(packet.getSchedule());
            if(!scheduleValidation) {
                model.addAttribute("message", "스케쥴 값을 정확히 입력해주세요");
                return "device/error";
            }
        } catch (ParseException e) {
            model.addAttribute("message", "스케쥴 값을 정확히 입력해주세요");
            // TODO Auto-generated catch block
            return "device/error";
        }



        deviceService.setNewOrder(deviceId, makePacketList(channel, packet), channel);
        model.addAttribute("message", "스케쥴 모드 모드 설정이 완료되었습니다.");
        return "user/order_add_success";
    }


    @GetMapping(value = "/user/autoMode")
    public String autoMode(HttpServletRequest request, ModelMap model, @RequestParam("deviceId") String deviceId,  @RequestParam("channel") int channel,
                           @RequestParam(name = "autoTimeLimit", required = false, defaultValue = "0") int autoTimeLimit){

        if(channel <= 0 || !StringUtils.hasLength(deviceId)){
            //TODO 예외처리 하기
            return "user/order_add_success";
        }



        String referer = request.getHeader("referer");
        model.addAttribute("referer", referer);

        Packet packet = Packet.builder().mode("a").autoTimeLimit(autoTimeLimit).build();
        deviceService.setNewOrder(deviceId, makePacketList(channel, packet), channel);
        model.addAttribute("message", "오토 모드 설정 완료되었습니다.");
        return "user/order_add_success";
    }

    @GetMapping(value = "/user/offMode")
    public String offMode(HttpServletRequest request, ModelMap model, @RequestParam("deviceId") String deviceId, @RequestParam("channel") int channel){
        if(channel <= 0 || !StringUtils.hasLength(deviceId)){
            //TODO 예외처리 하기
            return "ERROR";
        }

        String referer = request.getHeader("referer");
        model.addAttribute("referer", referer);

        Packet packet = Packet.builder().mode("0").build();
        deviceService.setNewOrder(deviceId, makePacketList(channel, packet), channel);
        model.addAttribute("message", "전원 종료 설정이 완료되었습니다.");
        return "user/order_add_success";
    }

    @GetMapping(value = "/user/onMode")
    public String onMode(HttpServletRequest request, ModelMap model, @RequestParam("deviceId") String deviceId, @RequestParam("channel") int channel){
        if(channel <= 0 || !StringUtils.hasLength(deviceId)){
            //TODO 예외처리 하기
            return "ERROR";
        }

        String referer = request.getHeader("referer");
        model.addAttribute("referer", referer);

        Packet packet = Packet.builder().mode("1").build();
        deviceService.setNewOrder(deviceId, makePacketList(channel, packet), channel);
        model.addAttribute("message", "전원 ON 설정이 완료되었습니다.");
        return "user/order_add_success";
    }

    @GetMapping(value = "/user/repeatMode")
    public String repeatMode(HttpServletRequest request, ModelMap model, @RequestParam("deviceId") String deviceId, @RequestParam("channel") int channel, @RequestParam("schedule") String schedule){
        if(channel <= 0 || !StringUtils.hasLength(deviceId)){
            //TODO 예외처리 하기
            return "ERROR";
        }

        String referer = request.getHeader("referer");
        model.addAttribute("referer", referer);

        Packet packet = Packet.builder().mode("r").schedule(schedule).build();
        deviceService.setNewOrder(deviceId, makePacketList(channel, packet), channel);
        model.addAttribute("message", "반복 모드 설정이 완료되었습니다.");
        return "user/order_add_success";
    }


    private static List<Packet> makePacketList(int channel, Packet packet){
        List<Packet> packets = new ArrayList<>();
        for(int k = 1; k <= 3; k++){
            if(channel == k){
                packets.add(packet);
            }else{
                packets.add(Packet.builder().mode("x").build());
            }
        }
        return packets;
    }

    private static String checkPacketHasAutoModeMsg(List<Packet> packets){
        String msg = "";
        for(int k = 0; k < 3; k++){
            if(StringUtils.hasLength(packets.get(k).getAutoModeMsg())){
                msg += ("채널" + (k+1) + ": " + packets.get(k).getAutoModeMsg() + " ");
            }
        }
        return msg;
    }
}
