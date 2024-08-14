package com.pettoyou.server.constant.exception;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final CustomResponseStatus customResponseStatus;


    public CustomException(CustomResponseStatus customResponseStatus) {

        super(customResponseStatus.getMessage());
        this.customResponseStatus = customResponseStatus;
    }
}


