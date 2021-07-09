package com.boot.smartrelay;

import com.boot.smartrelay.beans.ResponseBox;

public class Utils {
    public static ResponseBox makeResponseBox(boolean status, String message){
        return ResponseBox.builder()
                .status(status)
                .message(message)
                .build();
    }
}
