package com.boot.smartrelay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.boot.smartrelay.service.UserService;

@SpringBootTest
public class RemoveTest {
	
	@Autowired private UserService userService;
	
	@Test
	@DisplayName("remove large sector of ui")
	void removeLargeSectorTest() {
		userService.removeLargeSector("test-user", "GS강서타워");
	}
	
	@Test
	@DisplayName("remove small sector of ui")
	void removeSmallSectorTest() {
		userService.removeSmallSector("test-user", "LG타워", "1층");
	}
	
	@Test
	@DisplayName("modify large sector of ui")
	void modifyLargeSectorTest() {
		userService.modifyLargeSector("gs-user", "GS타워","LG타워");
	}
	
	
	@Test
	@DisplayName("modify small sector of ui")
	void modifySmallSectorTest() {
		userService.modifySmallSector("gs-user", "LG타워","1층", "12층");
			
	}
	
	
}
