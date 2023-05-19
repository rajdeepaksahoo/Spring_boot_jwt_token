package com.jwt.Spring_boot_jwt_token.jwtutil;

import com.jwt.Spring_boot_jwt_token.userdetails.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtUtils implements Serializable {
    @Value("${secretKet}")
    private String secretKey;
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);

    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> calimsRecover) {
        final Claims claims =   getAllClaimsFromToken(token);
        return calimsRecover.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody();
    }

    public  String generateToken(org.springframework.security.core.userdetails.UserDetails userDetails){
        Map<String,Object> claims =new HashMap<>();
        return doGenerteToken(claims,userDetails.getUsername());
    }
    private boolean isTokenExpired(String token){
        final Date expire = getExpirationFromToken(token);
        return expire.before(new Date());
    }

    private String doGenerteToken(Map<String, Object> claims, String name) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+2*1000)
                )
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public boolean isValid(String token, UserDetails userDetails){
        String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getName())&& !isTokenExpired(token));
    }

}
