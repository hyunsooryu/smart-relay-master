package com.boot.smartrelay;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.boot.smartrelay.beans.SmartRelayDevice;
import com.boot.smartrelay.service.AdminService;

@SpringBootTest
public class MongoTest {
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	AdminService adminService;
	
	@Test
	void test() {
		SmartRelayDevice device = new SmartRelayDevice();
		device.setDeviceId("device-456");
		mongoTemplate.save(device, "smartRelayDevice");
		device.setDeviceId("device-789");
		mongoTemplate.save(device, "smartRelayDevice");
	}
	
	@Test
	void existTest() {
		List<String> tmp = new ArrayList<>();
		tmp.add("device-200");
		tmp.add("device-100");
		boolean result = adminService.isExistDeviceIds(tmp);
		System.out.println(result);
	}
	
}
