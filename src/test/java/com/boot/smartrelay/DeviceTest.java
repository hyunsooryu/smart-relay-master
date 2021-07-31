package com.boot.smartrelay;

import com.boot.smartrelay.beans.Device;
import com.boot.smartrelay.beans.DeviceStatus;
import com.boot.smartrelay.beans.Packet;
import com.boot.smartrelay.repository.DeviceStatusMemoryRepository;
import com.boot.smartrelay.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DeviceTest {
    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceStatusMemoryRepository repository;
    /**
    @Test
    void insertOrder(){
       List<Packet> packets = new ArrayList<>();
       packets.add(Packet.builder()
                         .mode("0")
                         .schedule("22002300/")
                         .period("10")
                         .build()
       );
        packets.add(Packet.builder()
                .mode("0")
                .schedule("22002300/")
                .period("10")
                .build()
        );
        packets.add(Packet.builder()
                .mode("0")
                .schedule("22002300/")
                .period("10")
                .build()
        );

       deviceService.setNewOrder("device-1", packets, 1);
        var order = deviceService.getOrderIfPresent("device-1");
        System.out.println(order);
    }

    @Test
    void putStatusOfDevice(){
        List<Packet> packets = new ArrayList<>();
        packets.add(Packet.builder()
                .mode("0")
                .schedule("22002300/")
                .currentState(1)
                .build()
        );
        packets.add(Packet.builder()
                .mode("0")
                .schedule("22002300/")
                .currentState(1)
                .build()
        );
        packets.add(Packet.builder()
                .mode("0")
                .schedule("22002300/")
                .currentState(1)
                .build()
        );
        deviceService.setDeviceStatus("device-1", packets);
    }

    **/

}
