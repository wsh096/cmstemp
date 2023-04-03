package com.zerobase.user.client;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SendMailForm {
    private String from;
    private String to;
    private String subject;
    private String text;
}
