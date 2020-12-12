package com.lab.api.configuration;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.lab.api.common.constants.ApplicationConstants;
import com.lab.api.common.logger.LabApiLogger;
import com.lab.api.repository.entity.user.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JasonWebTokenUtil {

  @Value("${jwt.secret.key}")
  private String jwtSecretKey;

  @Value("${jwt.expiration.duration}")
  private String jwtExpirationDuration;

  public String generateAccessToken(CustomUserDetail customUser) {
    return Jwts.builder().setSubject(customUser.getUsername())
        .setIssuer(ApplicationConstants.ROOT_PACKAGE).setIssuedAt(new Date())
        .setExpiration(
            new Date(System.currentTimeMillis() + Integer.parseInt(jwtExpirationDuration)))
        .signWith(SignatureAlgorithm.HS512, jwtSecretKey).compact();
  }

  public String getUserId(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();

    return claims.getSubject().split(",")[0];
  }

  public String getUsername(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();

    return claims.getSubject().split(",")[1];
  }

  public Date getExpirationDate(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();

    return claims.getExpiration();
  }

  public boolean validate(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
      return true;
    } catch (SignatureException ex) {
      LabApiLogger.error(this.getClass(), "Invalid JWT signature - {}", ex);
    } catch (MalformedJwtException ex) {
      LabApiLogger.error(this.getClass(), "Invalid JWT token - {}", ex);
    } catch (ExpiredJwtException ex) {
      LabApiLogger.error(this.getClass(), "Expired JWT token - {}", ex);
    } catch (UnsupportedJwtException ex) {
      LabApiLogger.error(this.getClass(), "Unsupported JWT token - {}", ex);
    } catch (IllegalArgumentException ex) {
      LabApiLogger.error(this.getClass(), "JWT claims are empty - {}", ex);
    }
    return false;
  }
}
