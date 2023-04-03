package com.zerobase.user.web;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.user.application.MailApplication;
import com.zerobase.user.domain.form.OrderListMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail/send")
public class MailController {
    private final MailApplication mailApplication;
    private final JwtAuthenticationProvider provider;

    @PostMapping("/orderlist")
    public ResponseEntity<Void> sendmailOfOrderList(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                    @RequestBody OrderListMailForm orderListMailForm) {
        mailApplication.sendmailOfOrderList(provider.getUserVo(token).getId(), orderListMailForm);
        return ResponseEntity.ok().build();
    }
}
