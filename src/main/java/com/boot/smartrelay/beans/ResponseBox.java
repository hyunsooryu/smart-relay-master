package com.boot.smartrelay.beans;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseBox {
    private boolean status;
    private String message;
}
