package com.boot.smartrelay.validator;

import com.boot.smartrelay.beans.AdminUser;
import com.boot.smartrelay.config.AdminUserConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AdminValidator implements Validator {

    final AdminUserConfigProperties adminUserConfigProperties;

    @Override
    public boolean supports(Class<?> aClass) {
       return AdminUser.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AdminUser adminUser = (AdminUser)o;
        if(!adminUserConfigProperties.getUserId().equals(adminUser.getId())){
            errors.reject("아이디가 일치 하지 않습니다.");
            return;
        }

        if(!adminUserConfigProperties.getUserPw().equals(adminUser.getPassword())){
            errors.reject("비밀번호가 일치 하지 않습니다.");
            return;
        }

    }
}
