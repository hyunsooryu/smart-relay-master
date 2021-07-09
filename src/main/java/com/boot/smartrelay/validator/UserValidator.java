package com.boot.smartrelay.validator;

import com.boot.smartrelay.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
       return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        String beanName = errors.getObjectName();
        if("deviceUser".equals(beanName)){
            User deviceUser = (User)o;
            if(deviceUser.getId() == null || deviceUser.getPassword() == null){
                errors.reject("null error");
            }
            else if("".equals(deviceUser.getId()) || "".equals(deviceUser.getPassword())){
                errors.reject("no val error");
            }
        }
    }
}
