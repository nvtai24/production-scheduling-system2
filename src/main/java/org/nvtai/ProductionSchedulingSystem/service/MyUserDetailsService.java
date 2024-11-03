package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.entity.*;
import org.nvtai.ProductionSchedulingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user by username: " + username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!user.getActive()) {
            throw new UsernameNotFoundException("User is not active");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            System.out.println("Role: " + role.getRname());
            System.out.println("Number of features: " + role.getFeatures().size());
            role.getFeatures().forEach(feature -> {
                // Kết hợp URL và phương thức HTTP để phân biệt quyền
                String authority = feature.getUrl() + ":" + feature.getMethod();
                grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                System.out.println("Granted Authority: " + authority);
            });
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuthorities);
    }



}