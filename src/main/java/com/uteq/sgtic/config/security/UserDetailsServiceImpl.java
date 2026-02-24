package com.uteq.sgtic.config.security;

import com.uteq.sgtic.repository.RoleRepository;
import com.uteq.sgtic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserRepository.UserCredentialsProjection credentials = userRepository
                .findCredentialsByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!credentials.getIsActive()) {
            throw new UsernameNotFoundException("User is inactive");
        }

        List<String> roleNames = roleRepository.findRoleNamesByUserId(credentials.getUserId());

        List<SimpleGrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                credentials.getEmail(),
                credentials.getPasswordHash(),
                authorities
        );
    }
}
