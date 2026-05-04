package com.aans.portfolio.security;

import com.aans.portfolio.entities.AppUser;
import com.aans.portfolio.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser u =
        appUserRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    String role = "ROLE_" + u.getRole().name();
    return User.builder()
        .username(u.getUsername())
        .password(u.getPassword())
        .authorities(new SimpleGrantedAuthority(role))
        .build();
  }
}
