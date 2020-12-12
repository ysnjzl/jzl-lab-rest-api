package com.lab.api.service.user;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.lab.api.repository.UserRepository;
import com.lab.api.repository.entity.user.CustomUserDetail;
import com.lab.api.repository.entity.user.User;

@Service
public class UserService implements UserDetailsService  {
  private UserRepository userRepository;
  
  @Autowired
  public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = this.userRepository.findById(username);
    // TODO Attach roles
    if (userOptional.isEmpty()) {
      throw new UsernameNotFoundException("################## USER NOT FOUND ##################");
    }
    return new CustomUserDetail(userOptional.get());
  }
}
