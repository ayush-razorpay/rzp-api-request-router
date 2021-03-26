package com.rzp.apirouter.dto;

import lombok.Data;

@Data
public class ControllerResponseDto {

    private String message;
    private String status;


    private ControllerResponseDto(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public static ControllerResponseDto of(String message, String status) {
        return new ControllerResponseDto(message, status);
    }
}
