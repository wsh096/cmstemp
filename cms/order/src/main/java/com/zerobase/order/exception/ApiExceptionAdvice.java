package com.zerobase.order.exception;


import com.zerobase.order.exception.CustomException.CustomExceptionResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<CustomException.CustomExceptionResponse> exceptionHandler(
            HttpServletRequest request, final CustomException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(CustomExceptionResponse.builder()
                        .status(e.getStatus())
                        .message(e.getMessage())
                        .code(e.getErrorCode().name())
                        .build());
    }


}
