package com.lab.api.common.model;

public class UserDto {

  private String username;
  private String password;
  private boolean enabled;

  public UserDto() {
    super();
  }

  public UserDto(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }
  
  public UserDto(String username, boolean enabled) {
    super();
    this.username = username;
    this.enabled = enabled;
  }

  public UserDto(String username, String password, boolean enabled) {
    super();
    this.username = username;
    this.password = password;
    this.enabled = enabled;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

}
