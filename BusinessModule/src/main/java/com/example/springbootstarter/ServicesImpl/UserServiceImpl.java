package com.example.springbootstarter.ServicesImpl;

import com.example.springbootstarter.Entities.Users;
import com.example.springbootstarter.Repositories.UserRepository;
import com.example.springbootstarter.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl extends GenericServiceImpl<Users> implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl() {
        super(Users.class);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Loading user by email
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            // Throwing error if user is not found
            throw new UsernameNotFoundException("User with this email does not exist");
        } else{
            // if user found, collecting authorities and returning User object
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
        }
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        // finding user by email
        return userRepository.findByEmail(email);
    }

    @Override
    public void doSomething() throws Exception {
        // add your business logic here
    }
}
