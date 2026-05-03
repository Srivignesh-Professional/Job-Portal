package com.jobportal.findworks.security.auth;

import com.jobportal.findworks.repository.UserRepository;
import com.jobportal.findworks.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        var user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for phone: " + phone));
        return new UserPrincipal(user);
    }
}