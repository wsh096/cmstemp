package com.zerobase.user.application;

import com.zerobase.user.client.MailgunClient;
import com.zerobase.user.client.SendMailForm;
import com.zerobase.user.domain.form.OrderListMailForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class MailApplication {
    private final MailgunClient mailgunClient;
    private final CustomerService customerService;
    public void sendmailOfOrderList(Long id, OrderListMailForm orderListMailForm) {
        Customer customer = customerService
                .findByIdAndEmail(id, orderListMailForm.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        SendMailForm sendMailForm = SendMailForm.builder()
                .from("tekhanst@gmail.com")
                .to(customer.getEmail())
                .subject("주문 내역입니다.")
                .text(orderListMailForm.getProductInfos().stream()
                        .map(productInfo -> productInfo.makeText())
                        .collect(Collectors.joining()))
                .build();

        mailgunClient.sendEmail(sendMailForm);
    }


}
