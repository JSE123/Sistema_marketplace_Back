package com.marketplace.MarketBack.exception.custom;

import com.marketplace.MarketBack.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    // Getter para errorCode
    private ErrorCode errorCode; // Opcional

    public NotFoundException(String message) {
        super(message);
    }

    // Constructor con código de error (opcional)
    public NotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
