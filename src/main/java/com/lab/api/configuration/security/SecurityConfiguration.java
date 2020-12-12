package com.lab.api.configuration.security;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.lab.api.common.logger.LabApiLogger;
import com.lab.api.configuration.JasonWebTokenFilter;
import com.lab.api.service.user.UserService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${lab.api-docs.path}")
  private String restApiDocPath;

  @Value("${lab.swagger-ui.path}")
  private String swaggerPath;

  private UserService customUserDetailsService;

  private final JasonWebTokenFilter jasonWebTokenFilter;

  @Autowired
  public SecurityConfiguration(UserService customUserDetailsService,
      JasonWebTokenFilter jasonWebTokenFilter) {
    this.customUserDetailsService = customUserDetailsService;
    this.jasonWebTokenFilter = jasonWebTokenFilter;
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(this.customUserDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Enable CORS and disable CSRF
    http = http.cors().and().csrf().disable();

    // Set session management to stateless
    http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

    // Set unauthorized requests exception handler
    http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
      LabApiLogger.error(this.getClass(), "Unauthorized request - {}", ex);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }).and();

    // Set permissions on endpoints
    http.authorizeRequests()
        // Swagger endpoints must be publicly accessible
        .antMatchers("/").permitAll().antMatchers(String.format("%s/**", restApiDocPath))
        .permitAll().antMatchers(String.format("%s/**", swaggerPath)).permitAll()
        // Our public endpoints
        .antMatchers("/api/public/**").permitAll().antMatchers(HttpMethod.GET, "/api/author/**")
        .permitAll().antMatchers(HttpMethod.POST, "/api/author/search").permitAll()
        .antMatchers(HttpMethod.GET, "/api/book/**").permitAll()
        .antMatchers(HttpMethod.POST, "/api/book/search").permitAll()
        // Our private endpoints
        .antMatchers("/api/admin/user/**").hasRole("USER")
        .antMatchers("/api/author/**").hasRole("ADMIN")
        .antMatchers("/api/book/**").hasRole("WRITER")
        .anyRequest().authenticated();

    // Add JWT token filter
    http.addFilterBefore(jasonWebTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }

  // Used by spring security if CORS is enabled.
  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  // Expose authentication manager bean
  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


}
