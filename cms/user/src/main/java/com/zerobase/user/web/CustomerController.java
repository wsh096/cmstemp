package com.zerobase.user.web;

import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.user.domain.dto.CustomerDto;
import com.zerobase.user.domain.form.ChangeBalanceForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.service.customer.CustomerBalanceService;
import com.zerobase.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;
    @GetMapping("/getinfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token){
        UserVo vo = provider.getUserVo(token);
        Customer c = customerService.findByIdAndEmail(vo.getId(),vo.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return ResponseEntity.ok(CustomerDto.from(c));
    }
    @PostMapping("/balance")
    public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                 @RequestBody ChangeBalanceForm form) {

        UserVo vo = provider.getUserVo(token);

        return ResponseEntity.ok(customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());
    }
}