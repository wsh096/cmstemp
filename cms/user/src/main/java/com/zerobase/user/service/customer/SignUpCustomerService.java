package com.zerobase.user.service.customer;

import com.zerobase.user.domain.form.SignUpForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

import static com.zerobase.user.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {
    private final CustomerRepository customerRepository;
    public Customer signUp(SignUpForm form){
        return customerRepository.save(Customer.from(form));
    }
    public boolean isEmailExist(String email){
        return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .isPresent();
    }
    @Transactional
    public void verifyEmail(String email, String code) {
        Customer customer = customerRepository
                .findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        verifyOfCustomer(customer);
        equalsVerificationCode(customer, code);
        verifyExpiredAtBefore(customer);
        customer.setVerify(true);
    }
    private void verifyOfCustomer(Customer customer) {
        if(customer.isVerify()) {
            throw new CustomException(ALREADY_VERIFY);
        }
    }
    private void equalsVerificationCode(Customer customer, String code) {
        if(!customer.getVerificationCode().equals(code)) {
            throw new CustomException(WRONG_VERIFICATION);
        }
    }
    private void verifyExpiredAtBefore(Customer customer) {
        if(customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(EXPIRE_CODE);
        }
    }
    @Transactional
    public LocalDateTime changeCustomerValidateEmail(Long customerId,
                                                     String verificationCode){
        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        customer.setVerificationCode(verificationCode);
        customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
        return customer.getVerifyExpiredAt();
    }
}
