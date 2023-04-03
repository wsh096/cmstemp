package com.zerobase.user.web;
import com.zerobase.user.application.SignInApplication;
import com.zerobase.user.domain.form.SignInForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signin")
public class SignInController {
    private final SignInApplication signInApplication;
    @PostMapping("/customer")
    public ResponseEntity<String> signCustomer(@RequestBody SignInForm form){
        return ResponseEntity.ok(signInApplication.customerLoginToken(form));
    }
    @PostMapping("/seller")
    public ResponseEntity<String> signSeller(@RequestBody SignInForm form){
        return ResponseEntity.ok(signInApplication.sellerLoginToken(form));
    }

}
