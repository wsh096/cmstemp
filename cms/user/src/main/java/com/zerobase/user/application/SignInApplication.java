package com.zerobase.user.application;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.user.domain.form.SignInForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.model.Seller;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.service.customer.CustomerService;
import com.zerobase.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.domain.common.UserType.CUSTOMER;
import static com.zerobase.domain.common.UserType.SELLER;
import static com.zerobase.user.exception.ErrorCode.LOGIN_CHECK_FAIL;

@Service
@RequiredArgsConstructor
public class SignInApplication {
    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;
    private final SellerService sellerService;

    public String customerLoginToken(SignInForm form) {
        //1. 로그인 가능 여부
        Customer c = customerService
                .findValidCustomer(form.getEmail(),
                        form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        return provider.createToken(c.getEmail(), c.getId(), CUSTOMER);
    }

    public String sellerLoginToken(SignInForm form) {
        Seller s = sellerService.findValidSeller(form.getEmail(),
                        form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        return provider.createToken(s.getEmail(), s.getId(), SELLER);
    }

}
