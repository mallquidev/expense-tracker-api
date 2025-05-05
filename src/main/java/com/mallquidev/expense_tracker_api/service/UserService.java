package com.mallquidev.expense_tracker_api.service;

import com.mallquidev.expense_tracker_api.entities.User;
import com.mallquidev.expense_tracker_api.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    //5
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.singleton(authority)
        );
    }

    //buscamos un usuario
    public User findByUserName(String userName){
        return  userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

    }

    //existe el usuario?
    public boolean existsByUserName(String userName){
        return userRepository.existsByUserName(userName);
    }

    //guardar
    public void save(User user){
        userRepository.save(user);
    }

}
