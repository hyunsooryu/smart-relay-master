package com.boot.smartrelay.controller;

import javax.annotation.Resource;

import com.boot.smartrelay.beans.ResponseBox;
import com.boot.smartrelay.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.boot.smartrelay.beans.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/modify")
@RequiredArgsConstructor
public class ApiController {
	
	@Resource(name="loginUser")
    private User loginUser;

	final UserService userService;

	@PostMapping("/modifyLargeSector")
	public String modifyLargeSector(ModelMap model, @RequestParam String largeSector, @RequestParam String newLargeSector) {

		if(null == newLargeSector || "".equals(newLargeSector)){
			model.addAttribute("message", "장소를 정확히 입력해주세요");
			return "user/large_device_fail";
		}

		ResponseBox result = userService.modifyLargeSector(loginUser.getId(), largeSector, newLargeSector);

		if(!result.isStatus()){
			model.addAttribute("message", result.getMessage());
			return "user/large_device_fail";
		}
		model.addAttribute("message", "성공적으로 수정되었습니다.");
		return "user/large_device_success";

	}
	
	@PostMapping("/modifySmallSector")
	public String modifySmallSector(ModelMap model, @RequestParam String largeSector, @RequestParam String smallSector, @RequestParam String newSmallSector) {
		ResponseBox result = userService.modifySmallSector(loginUser.getId(), largeSector, smallSector, newSmallSector);
		if(!result.isStatus()){
			model.addAttribute("message", result.getMessage());
			return "user/crud_device_fail";
		}
		model.addAttribute("message", "성공적으로 수정되었습니다.");
		return "user/crud_device_success";
	}

	@PostMapping("/removeLargeSector")
	public String removeLargeSector(ModelMap model, @RequestParam("largeSector") String largeSector) {
		ResponseBox result = userService.removeLargeSector(loginUser.getId(), largeSector);

		if(!result.isStatus()){
			model.addAttribute("message", result.getMessage());
			return "user/large_device_fail";
		}
		model.addAttribute("message", "성공적으로 삭제되었습니다.");
		return "user/large_device_success";

	}

	@PostMapping("/removeSmallSector")
	public String removeSmallSector(ModelMap model, @RequestParam String largeSector, @RequestParam String smallSector) {
		ResponseBox result = userService.removeSmallSector(loginUser.getId(), largeSector, smallSector);
		if(!result.isStatus()){
			model.addAttribute("message", result.getMessage());
			return "user/crud_device_fail";
		}
		model.addAttribute("message", "성공적으로 삭제되었습니다.");
		return "user/crud_device_success";
	}

}
