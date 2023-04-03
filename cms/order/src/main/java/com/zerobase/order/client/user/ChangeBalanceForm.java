package com.zerobase.order.client.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBalanceForm {
    private String from;
    private String message;
    private Integer money;
}

