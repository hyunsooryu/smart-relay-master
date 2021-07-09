package com.boot.smartrelay.beans;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "userDevice")
public class Device {
    @Id
    private String deviceId;
    private String userId;
    private String largeSector;
    private String smallSector;
}
