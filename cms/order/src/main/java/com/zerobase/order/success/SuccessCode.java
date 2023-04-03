package com.zerobase.order.success;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Getter
public enum SuccessCode {
    THANK_YOU_FOR_YOUR_ORDER(HttpStatus.OK, "주문에 감사드립니다.");
    private final HttpStatus httpStatus;
    private final String detail;
}
