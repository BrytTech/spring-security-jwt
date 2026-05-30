package org.bright.springsecurity.service;

import org.bright.springsecurity.entity.Role;
import org.bright.springsecurity.entity.UserEntity;
import org.bright.springsecurity.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity saveUserWithEncodedPassword(String username, String rawPassword, Role role){
        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setRole(role);

        return userRepository.save(user);
    }

    public UserEntity getUserFromUsername(String username){
        return userRepository
                .findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getUserFromUsername(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        user.getRole().getPermissions().forEach(permission ->{
            authorities.add(new SimpleGrantedAuthority(permission.name()));
        } );
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
