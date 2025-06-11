package com.example.springbootstarter.Services;


import com.example.springbootstarter.Entities.Users;
import java.util.Optional;

public interface UserService{

    Optional<Users> getUserByEmail(String email);

    void doSomething() throws Exception;
}
