package com.dju.lounge.global.auth;

import com.dju.lounge.domain.user.model.Users;
import com.dju.lounge.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));

        return new CustomUserDetail(user, user.getId());
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        Users user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User " + id + " not found"));

        return new CustomUserDetail(user, user.getId());
    }
}
