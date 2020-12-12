package com.lab.api.repository.entity.user;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetail implements UserDetails {

  private static final long serialVersionUID = -5748183882926432286L;

  private User user;

  public CustomUserDetail() {}

  public CustomUserDetail(User user) {
    // TODO Validate user
    this.user = user;
  }



  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO Validate user
    return Arrays.asList(new SimpleGrantedAuthority("USER"));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }

}
