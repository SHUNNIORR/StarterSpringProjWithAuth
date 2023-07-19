package com.shunnior.springsecurity.services;

import com.shunnior.springsecurity.repositories.UserRepository;
import com.shunnior.springsecurity.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userDetails =  userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("El usuario"+username+" no existe"));
        Collection<? extends GrantedAuthority> authorities = userDetails.getRoles()
                .stream()
                .map(roleEntity -> new SimpleGrantedAuthority("ROLE_".concat(roleEntity.getName().name())))
                .collect(Collectors.toSet());
        return new User(userDetails.getUsername(),
                userDetails.getPassword(),
                true,
                true,
                true,
                true,
                authorities
                );
    }
}
