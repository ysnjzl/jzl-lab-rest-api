package com.lab.api.rest.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lab.api.common.model.UserDto;
import com.lab.api.configuration.JasonWebTokenUtil;
import com.lab.api.repository.entity.user.CustomUserDetail;
import com.lab.api.repository.entity.user.User;
import com.lab.api.service.user.UserService;

@RestController
@RequestMapping(path = "/api/public")
public class AuthenticationRestConstroller {

  private AuthenticationManager authenticationManager;
  private JasonWebTokenUtil jasonWebTokenUtil;
  // private UserService userService;

  @Autowired
  public AuthenticationRestConstroller(AuthenticationManager authenticationManager,
      JasonWebTokenUtil jasonWebTokenUtil, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jasonWebTokenUtil = jasonWebTokenUtil;
    // this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody User requestedUsed) {
    try {
      Authentication authenticate =
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              requestedUsed.getUsername(), requestedUsed.getPassword()));

      CustomUserDetail customUser = (CustomUserDetail) authenticate.getPrincipal();

      return ResponseEntity.ok()
          .header(HttpHeaders.AUTHORIZATION, jasonWebTokenUtil.generateAccessToken(customUser))
          .body(new UserDto(customUser.getUsername(), customUser.isEnabled()));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
