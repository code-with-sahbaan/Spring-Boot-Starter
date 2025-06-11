package com.example.springbootstarter.Bootstraps;

import com.example.springbootstarter.Entities.Users;
import com.example.springbootstarter.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataBootstrapping implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        /*
         * Adding a user for testing with
         * email: abc@example.com
         * password: password
         * */
        Users users = new Users();
        users.setEmail("abc@example.com");
        users.setPassword(new BCryptPasswordEncoder().encode("password"));
        users.setFullName("My User 01");
        userRepository.save(users);
    }
}
