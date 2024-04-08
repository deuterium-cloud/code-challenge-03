package cloud.deuterium.wb.security;

import cloud.deuterium.wb.exceptions.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtService {

  private static final int MINUTES = 60;
  private final String keyString;

    public JwtService(@Value("${app.secret.key}") String keyString) {
        this.keyString = keyString;
    }

    public Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(keyString);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String email) {
    var now = Instant.now();
    return Jwts.builder()
        .subject(email)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
        .signWith(getSigningKey())
        .compact();
  }

  public String extractUsername(String token) {
    return getTokenBody(token).getSubject();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public Claims getTokenBody(String token) {
    try {
      return Jwts
          .parser()
          .setSigningKey(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
      throw new AccessDeniedException("Access denied: " + e.getMessage());
    }
  }

  private boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }
}
