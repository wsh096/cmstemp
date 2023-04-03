package com.zerobase.user.application;

import com.zerobase.user.client.MailgunClient;
import com.zerobase.user.client.SendMailForm;
import com.zerobase.user.domain.form.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.model.Seller;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.service.customer.SignUpCustomerService;
import com.zerobase.user.service.seller.SignUpSellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import static com.zerobase.user.exception.ErrorCode.ALREADY_REGISTER_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;
    private final SignUpSellerService signUpSellerService;

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }
    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ALREADY_REGISTER_USER);
        } else {
            Customer c = signUpCustomerService.signUp(form);

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tekhanst@gmail.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(
                            form.getEmail(),
                            form.getName(),
                            "customer"
                            , code))
                    .build();
            log.info("Send email result : " +mailgunClient.sendEmail(sendMailForm));
            mailgunClient.sendEmail(sendMailForm);
            signUpCustomerService
                    .changeCustomerValidateEmail(c.getId(), code);
            return "회원 가입에 성공하였습니다.";
        }
    }

    public void sellerVerify(String email, String code) {
        signUpSellerService.verifyEmail(email, code);
    }

    public String sellerSignUp(SignUpForm form) {
        if (signUpSellerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ALREADY_REGISTER_USER);
        } else {
            Seller s = signUpSellerService.signUp(form);

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("tekhanst@gmail.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(
                            form.getEmail(),
                            form.getName(),
                            "seller",
                            code))
                    .build();
            log.info("Send email result : " +mailgunClient.sendEmail(sendMailForm));
            mailgunClient.sendEmail(sendMailForm);
            signUpSellerService.changeSellerValidateEmail(s.getId(), code);
            return "회원 가입에 성공하였습니다.";
        }
    }
    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }
    private String getVerificationEmailBody(String email, String name, String type, String code) {
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello ").append(name)
                .append("! Please Click Link for verficiation \n\n")
                .append("http://localhost:8081/signup/"+
                        type +
                        "/verify/?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }


}
