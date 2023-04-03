package com.zerobase.domain.config;

import com.zerobase.domain.common.UserType;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.util.Aes256Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationProvider {
    private final String secretKey = "secretKey";
    private final long tokenValidTime = 1000L * 60 * 60 * 24; //인증 만료 하루
    public String createToken(String userPk, Long id, UserType userType){
        Claims claims = Jwts.claims()
                .setSubject(Aes256Utils.encrypt(userPk))//커스텀 암호화 방식
                .setId(Aes256Utils.encrypt(id.toString()));
        claims.put("roles",userType);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256,secretKey)//기본 제공 암호화
                .compact();
    }
    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return !claimsJws.getBody()
                    .getExpiration()
                    .before(new Date());
        }catch (Exception e){
            return false;
        }
    }
    public UserVo getUserVo(String token){
        Claims c = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return new UserVo(
                Long.valueOf(
                        Objects.requireNonNull(
                                Aes256Utils.decrypt(c.getId())
                        )
                ), Aes256Utils.decrypt(c.getSubject())
        );
    }
}

