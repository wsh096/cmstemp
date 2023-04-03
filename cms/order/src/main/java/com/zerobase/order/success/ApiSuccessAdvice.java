package com.zerobase.order.success;


import com.zerobase.order.success.CustomSuccess.CustomSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiSuccessAdvice {
    @ExceptionHandler({CustomSuccess.class})
    public ResponseEntity<CustomSuccess.CustomSuccessResponse> exceptionHandler(
            HttpServletRequest request, final CustomSuccess s) {
        return ResponseEntity
                .status(s.getStatus())
                .body(CustomSuccessResponse.builder()
                        .status(s.getStatus())
                        .message(s.getMessage())
                        .code(s.getSuccessCode().name())
                        .build());
    }


}
