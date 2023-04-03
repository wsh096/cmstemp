package com.zerobase.user.config.filter;

import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/seller/*") //seller 대한 모든 패턴
@RequiredArgsConstructor
public class SellerFilter implements Filter {
    private final JwtAuthenticationProvider jwtAuthenticationprovider;
    private final SellerService sellerService;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-Auth-TOKEN");

        if (!jwtAuthenticationprovider.validateToken(token)) {
            throw new ServletException("Invalid Access");
        }
        UserVo vo = jwtAuthenticationprovider
                .getUserVo(token);
        sellerService
                .findByIdAndEmail(vo.getId(), vo.getEmail())// 괜찮은 토큰인지 확인
                .orElseThrow(() -> new ServletException("Invalid access"));

        chain.doFilter(request, response);
    }
}
