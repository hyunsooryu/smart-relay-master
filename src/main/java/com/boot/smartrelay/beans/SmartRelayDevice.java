package com.boot.smartrelay.beans;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "smartRelayDevice")
@Getter
@Setter
public class SmartRelayDevice {
	@Id
	String deviceId;
	String userId;

}
