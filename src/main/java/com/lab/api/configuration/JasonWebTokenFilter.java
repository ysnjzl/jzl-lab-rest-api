package com.lab.api.configuration;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.lab.api.common.constants.SecurityConstants;
import com.lab.api.service.user.UserService;

@Component
public class JasonWebTokenFilter extends OncePerRequestFilter {

  private JasonWebTokenUtil jasonWebTokenUtil;
  private UserService customUserDetailsService;

  @Autowired
  public JasonWebTokenFilter(JasonWebTokenUtil jasonWebTokenUtil,
      UserService customUserDetailsService) {
    this.jasonWebTokenUtil = jasonWebTokenUtil;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    // Get authorization header and validate
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.isEmpty(header) || !header.startsWith(SecurityConstants.BEARER)) {
      chain.doFilter(request, response);
      return;
    }

    // Get jwt token and validate
    final String token = header.split(" ")[1].trim();
    if (!jasonWebTokenUtil.validate(token)) {
      chain.doFilter(request, response);
      return;
    }

    // Get user identity and set it on the spring security context
    UserDetails userDetails =
        customUserDetailsService.loadUserByUsername(jasonWebTokenUtil.getUsername(token));

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }
}
