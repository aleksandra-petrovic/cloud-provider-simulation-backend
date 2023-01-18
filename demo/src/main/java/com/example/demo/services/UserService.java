package com.example.demo.services;


import com.example.demo.model.Permission;
import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private String jwt;


    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User findById(Long id) { return this.userRepository.findByUserId(id); }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.findByEmail(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("e-mail "+username+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getEmail(), myUser.getPassword(), new ArrayList<>());
    }

    public User addNewUser(String name, String surname, String email, String password, List<Permission> permissions){
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setPermissions(permissions);
        return this.userRepository.save(user);
    }

    public User editUser(User user, String name, String surname, String email, List<Permission> permissions){
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPermissions(permissions);
        return this.userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public User create(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}


