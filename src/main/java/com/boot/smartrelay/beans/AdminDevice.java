package com.boot.smartrelay.beans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "adminDevice")
@Getter
@Setter
public class AdminDevice {
    @Id
    public String id;
    public List<String> deviceIdList;
}
