package ru.sber.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import ru.sber.demo.repository.UserRepository;
import ru.sber.demo.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var person = repository.findByUsername(username);
        return person
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("user " + username + " not found"));
    }

}
