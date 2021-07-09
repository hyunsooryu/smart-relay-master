package com.boot.smartrelay.validator;


import com.boot.smartrelay.beans.Device;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DeviceValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Device.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Device device = (Device)o;
        if(device.getDeviceId() == null || device.getSmallSector() == null || device.getLargeSector() == null){
            errors.reject("null error");
        }
        if("".equals(device.getDeviceId())||
        "".equals(device.getSmallSector())||
        "".equals(device.getLargeSector())){
            errors.reject("blank error");
        }
    }
}
