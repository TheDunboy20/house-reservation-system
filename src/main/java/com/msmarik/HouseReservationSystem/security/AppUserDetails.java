package com.msmarik.HouseReservationSystem.security;

import com.msmarik.HouseReservationSystem.model.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class AppUserDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public AppUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getName();
        this.password = user.getPasswordHash();
        this.isEnabled = user.isEnabled();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
