package com.zerobase.user.web;

import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.user.domain.dto.SellerDto;
import com.zerobase.user.domain.model.Seller;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final JwtAuthenticationProvider provider;
    private final SellerService sellerService;
    @GetMapping("/getinfo")
    public ResponseEntity<SellerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token){
        UserVo vo = provider.getUserVo(token);
        Seller s = sellerService.findByIdAndEmail(vo.getId(),vo.getEmail())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return ResponseEntity.ok(SellerDto.from(s));
    }

}
