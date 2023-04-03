package com.zerobase.order.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.order.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CustomSuccess extends RuntimeException {
    private final SuccessCode successCode;
    private final int status;
    private static final ObjectMapper mapper = new ObjectMapper();
    public CustomSuccess(SuccessCode successCode) {
        super(successCode.getDetail());
        this.successCode = successCode;
        this.status = successCode.getHttpStatus().value();
    }
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    public static class CustomSuccessResponse {
        private int status;
        private String code;
        private String message;
    }
}
