package com.boot.smartrelay.service;

import com.boot.smartrelay.beans.*;
import com.boot.smartrelay.repository.DeviceStatusMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

   final DeviceStatusMemoryRepository deviceStatusMemoryRepository;

    @Override
    public boolean setDeviceStatus(String deviceId, List<Packet> packets) {
        PacketList packetListForStatus = PacketList.builder().deviceId(deviceId).packets(packets).build();
        boolean setResult = deviceStatusMemoryRepository.setDeviceStatus(packetListForStatus);
        return setResult;
    }

    @Override
    public boolean setNewOrder(String deviceId, List<Packet> packets, int channel) {
        boolean setResult = deviceStatusMemoryRepository.setDeviceOrder(deviceId, packets, channel - 1);
        return setResult;
    }

    @Override
    public List<Packet> getOrderIfPresent(String deviceId) {
        List<Packet> packets =  deviceStatusMemoryRepository.getOrderIfPresent(deviceId);
        return packets == null ? new ArrayList<Packet>() : packets;
    }

    @Override
    public PacketList getDeviceStatus(String deviceId) {
        PacketList packetListForStatus = deviceStatusMemoryRepository.getDeviceStatus(deviceId);
        return packetListForStatus == null ? new PacketList() : packetListForStatus;
    }

    @Override
    public boolean scheduledupcheck(String schedule) throws ParseException {

        boolean isTrue = true;

        String[] dayOfWeek = schedule.split("/");

        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");

        for(int i=0; i<7; i++) {

            String schOfEachDay[] = dayOfWeek[i].split(",");
            // schedule string 검증
            for(String sc : schOfEachDay) {
                if(!sc.equals("0")) {
                    if(!isInteger(sc)) {
                        System.out.println("숫자를 입력하세요.");
                        return false;
                    }


                    String startHr = sc.substring(0,2);
                    String startMin = sc.substring(2,4);
                    String endHr = sc.substring(4,6);
                    String endMin = sc.substring(6);

                    int iStartHr = Integer.parseInt(startHr);
                    int iStartMin = Integer.parseInt(startHr);
                    int iEndHr = Integer.parseInt(startHr);
                    int iEndMin = Integer.parseInt(startHr);

                    int startTime = Integer.parseInt(startHr + startMin);
                    int EndTime = Integer.parseInt(endHr + endMin);


                    if(startHr.length() != 2 || startMin.length() != 2 || endHr.length() != 2 || endMin.length() != 2) {
                        System.out.println("00:00 규격에 맞게 입력해주세요.");
                        return false;
                    }else if(iStartHr >= 24 || iStartHr < 0 || iStartMin >= 60 || iStartMin < 0
                            || iEndHr >= 24 || iEndHr < 0 || iEndMin >= 60 || iEndMin < 0) {
                        System.out.println("숫자를 정확히 입력하세요.");
                        return false;
                    }else if(Integer.compare(startTime, EndTime) == 1) {
                        System.out.println("시작 시간이 끝나는 시간보다 먼저 오게 설정돼야 합니다.");
                        return false;
                    }
                }


            }
            // 2개 이상의 스케줄이 입력됐을 때 입력값끼리 겹치는지 검증
            if(schOfEachDay.length >= 2) {
                for(int n=0; n<schOfEachDay.length; n++) {
                    for(int m=n+1; m<schOfEachDay.length; m++) {

                        Date leftStartTime, leftEndTime, rightStartTime, rightEndTime;

                        leftStartTime  = dateFormat.parse(schOfEachDay[n].substring(0,4));
                        leftEndTime    = dateFormat.parse(schOfEachDay[n].substring(4));
                        rightStartTime = dateFormat.parse(schOfEachDay[m].substring(0,4));
                        rightEndTime   = dateFormat.parse(schOfEachDay[m].substring(4));

                        isTrue = compareSchedule(leftStartTime, leftEndTime, rightStartTime, rightEndTime);

                        if(!isTrue) return false;



                    }
                }
            }
        }
        return true;
    }

    private boolean compareSchedule(Date firstStart, Date firstEnd, Date secondStart, Date secondEnd) {
        if(secondStart.after(firstStart) && secondStart.before(firstEnd) && secondEnd.after(firstStart) && secondEnd.before(firstEnd)) {
            log.info("시작 시간, 종료 시간이 모두 겹칩니다");
            return false;
        }else if(secondEnd.after(firstStart) && secondEnd.before(firstEnd)) {
            log.info("종료 시간이 겹칩니다");
            return false;
        }else if(secondStart.after(firstStart) && secondStart.before(firstEnd)) {
            log.info("시작 시간이 겹칩니다");
            return false;
        }else {
            log.info("시간이 겹치지 않으므로 insert 됩니다.");
            return true;
        }
    }

    private static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

}
