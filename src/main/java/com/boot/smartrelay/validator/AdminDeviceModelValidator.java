package com.boot.smartrelay.validator;

import com.boot.smartrelay.beans.AdminDeviceModel;
import com.boot.smartrelay.beans.Device;
import com.boot.smartrelay.beans.User;
import com.boot.smartrelay.service.AdminService;

import com.boot.smartrelay.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AdminDeviceModelValidator implements Validator {
	
	final AdminService adminService;

	final UserService userService;
	
    @Override
    public boolean supports(Class<?> aClass) {
        return AdminDeviceModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // 유저 생성 및 디바이스 등록 벨리데이션
        AdminDeviceModel adm = (AdminDeviceModel) o;

        //디바이스 추가 Validation
        if("modAdminDeviceModel".equals(errors.getObjectName())){
            //1. 아이디 문자열 // 디바이스 리스트 널 체크//
            if(!StringUtils.hasLength(adm.getUserId()) || adm.getDeviceId() == null){
               errors.reject("아이디 정보, 혹은 추가 될 디바이스 정보가 비어있으면 안됩니다.");
            }
            //1. - 1 디바이스 리스트 사이즈 체크
            if(adm.getDeviceId().size() == 0){
                errors.reject("아이디 정보, 혹은 추가 될 디바이스 정보가 비어있으면 안됩니다.");
            }
            //2. 해당 유저 존재 유무 체크
            User user = userService.findUserById(adm.getUserId());
            if(user == null){
                errors.reject("해당 유저가 존재하지 않습니다.");
            }
            //smartRelayDevice document 체크
            //3. 디바이스 존재 유무 체크
            if(adminService.isExistDeviceIds(adm.getDeviceId())){
                errors.reject("이미 존재하는 디바이스 아이디가 포함되어 있습니다.");
            }


        }

        //디바이스 신규 생성 Validation
        if("adminDeviceModel".equals(errors.getObjectName())){
            if (adm.getUserId() == null || adm.getUserPw() == null || adm.getDeviceId() == null || adm.getPhoneNumberOne() == null) {
                errors.reject("아이디, 비밀번호, 디바이스, 휴대폰 정보가 비어있으면 안됩니다.)");
            }
            if(adm.getUserId().equals("") || adm.getUserPw().equals("") || adm.getPhoneNumberOne().equals("")){
                errors.reject("아이디, 비밀번호, 디바이스, 휴대폰 정보가 비어있으면 안됩니다.");
            }
            if(adm.getDeviceId().size() == 0){
                errors.reject("아이디, 비밀번호, 디바이스, 휴대폰 정보가 비어있으면 안됩니다.");
            }
                 
            if(adminService.isExistDeviceIds(adm.getDeviceId())) {
            	errors.reject("이미 존재하는, 디바이스 아이디입니다.");
            }
        }

    }
}
