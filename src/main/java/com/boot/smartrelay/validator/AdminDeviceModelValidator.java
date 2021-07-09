package com.boot.smartrelay.validator;

import com.boot.smartrelay.beans.AdminDeviceModel;
import com.boot.smartrelay.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class AdminDeviceModelValidator implements Validator {
	
	final AdminService adminService;
	
    @Override
    public boolean supports(Class<?> aClass) {
        return AdminDeviceModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // 유저 생성 및 디바이스 등록 벨리데이션
        AdminDeviceModel adm = (AdminDeviceModel) o;

        if("adminDeviceModel".equals(errors.getObjectName())){
            if (adm.getUserId() == null || adm.getUserPw() == null || adm.getDeviceId() == null || adm.getPhoneNumberOne() == null) {
                errors.reject("아이디, 비밀번호, 디바이스 정보가 비어있으면 안됩니다.)");
            }
            if(adm.getUserId().equals("") || adm.getUserPw().equals("") || adm.getPhoneNumberOne().equals("")){
                errors.reject("아이디, 비밀번호, 디바이스 정보가 비어있으면 안됩니다.");
            }
            if(adm.getDeviceId().size() == 0){
                errors.reject("아이디, 비밀번호, 디바이스 정보가 비어있으면 안됩니다.");
            }
                 
            if(adminService.isExistDeviceIds(adm.getDeviceId())) {
            	errors.reject("이미 존재하는, 디바이스 아이디입니다.");
            }
        }

    }
}
