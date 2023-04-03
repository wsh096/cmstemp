package com.zerobase.user.client;


import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendService {
    private final MailgunClient mailgunClient;

    public Response sendEmail() {

        SendMailForm form = SendMailForm.builder()
                .from("tekhanst@gmail.com")
                .to("tekhanst@gmail.com")
                .subject("test email from")
                .text("my test")
                .build();
        Response response = mailgunClient.sendEmail(form);

        return response;
    }
}
