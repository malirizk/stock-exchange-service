package com.ing.stockexchangeservice.config;

import com.ing.stockexchangeservice.common.token.JwtSecretKey;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

  private final String HEADER_STRING = "Authorization";
  private final String TOKEN_PREFIX = "Bearer ";
  private final UserDetailsService userDetailsService;
  private final JwtSecretKey jwtSecretKey;

  public JwtRequestFilter(UserDetailsService userDetailsService, JwtSecretKey jwtSecretKey) {
    this.userDetailsService = userDetailsService;
    this.jwtSecretKey = jwtSecretKey;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String bearerToken = request.getHeader(HEADER_STRING);
    if (bearerToken != null) {
      String extractedEmail = null;
      try {
        extractedEmail =
            Jwts.parser()
                .setSigningKey(jwtSecretKey.getSecretKeyAsBytes())
                .parseClaimsJws(bearerToken.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
      } catch (SignatureException ex) {
        log.error("Invalid JWT signature", ex);
        throw ex;
      } catch (MalformedJwtException ex) {
        log.error("Invalid JWT token", ex);
        throw ex;
      } catch (ExpiredJwtException ex) {
        log.error("Expired JWT token", ex);
        throw ex;
      } catch (UnsupportedJwtException ex) {
        log.error("Unsupported JWT token", ex);
        throw ex;
      } catch (IllegalArgumentException ex) {
        log.error("JWT claims string is empty.", ex);
        throw ex;
      }

      UserDetails user = userDetailsService.loadUserByUsername(extractedEmail);

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    filterChain.doFilter(request, response);
  }
}
