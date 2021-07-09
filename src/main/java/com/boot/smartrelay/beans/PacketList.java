package com.boot.smartrelay.beans;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "packetList")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PacketList {
    @Id
    private String deviceId;

    List<Packet> packets;
}
