package com.msmarik.HouseReservationSystem.security;

import com.msmarik.HouseReservationSystem.model.entity.User;
import com.msmarik.HouseReservationSystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) {

        User appUser = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new AppUserDetails(appUser);
    }
}
