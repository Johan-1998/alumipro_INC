/* archivo de clase jwtservice */
package com.alumipro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final int expirationMinutes;

    public JwtService(
            @Value("${app.jwt.secret:ALUMIPRO_JWT_SECRET_CAMBIAR_EN_PROD_1234567890}") String secret,
            @Value("${app.jwt.expirationMinutes:480}") int expirationMinutes
    ) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(pad(bytes));
        this.expirationMinutes = expirationMinutes;
    }

    private byte[] pad(byte[] in) {
        if (in.length >= 32) return in;
        byte[] out = new byte[32];
        System.arraycopy(in, 0, out, 0, in.length);
        for (int i = in.length; i < out.length; i++) out[i] = (byte) (i * 31);
        return out;
    }

    public String createToken(String correo, String rol, Integer userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(expirationMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(correo)
                .claim("rol", rol)
                .claim("uid", userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
