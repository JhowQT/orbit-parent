package com.orbitbook.auth.security;

import com.orbitbook.auth.entity.UserOrbit;
import com.orbitbook.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        UserOrbit user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuário não encontrado"));

        return new User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(
                        new SimpleGrantedAuthority(
                                user.getRole()
                                        .getNameRole()
                        )
                )
        );
    }
}