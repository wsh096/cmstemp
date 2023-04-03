package com.zerobase.user.config.filter;

import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.user.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/customer/*") //customer에 대한 모든 패턴
@RequiredArgsConstructor
public class CustomerFilter implements Filter {
    private final JwtAuthenticationProvider jwtAuthenticationprovider;
    private final CustomerService customerService;

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
        customerService
                .findByIdAndEmail(vo.getId(), vo.getEmail())// 괜찮은 토큰인지 확인
                .orElseThrow(() -> new ServletException("Invalid access"));

        chain.doFilter(request, response);
    }
}
