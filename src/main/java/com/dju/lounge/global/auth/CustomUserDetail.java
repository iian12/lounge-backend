package com.dju.lounge.global.auth;

import com.dju.lounge.domain.user.model.Users;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomUserDetail implements UserDetails, OAuth2User {

    private final Users user;
    private final String userId;

    public CustomUserDetail(Users user, String userId) {
        this.user = user;
        this.userId = userId;
    }

    @Override
    public String getName() {
        return user != null ? user.getId() : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (user != null) {
            Map<String, Object> attributes = new HashMap<>();

            attributes.put("id", user.getId());
            attributes.put("email", user.getEmail());
            attributes.put("nickname", user.getNickname());
            attributes.put("role", user.getRole());
            attributes.put("profileImgUrl", user.getProfileImgUrl());

            return attributes;
        }

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user != null ? user.getEmail() : null;
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
        return true;
    }

    public String getUserId() {
        return userId;
    }

    public Users getUser() {
        return user;
    }

    public String getNickname() {
        return user != null ? user.getNickname() : null;
    }
}
