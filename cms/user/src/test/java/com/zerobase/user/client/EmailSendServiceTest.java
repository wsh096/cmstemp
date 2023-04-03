package com.zerobase.user.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfiguration
public class EmailSendServiceTest {
    @Autowired
    private EmailSendService emailSendService;
    @Test
    void emailTest() {
        emailSendService.sendEmail();
    }
}