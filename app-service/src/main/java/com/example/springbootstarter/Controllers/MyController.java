package com.example.springbootstarter.Controllers;

import com.example.springbootstarter.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/")
@RestController
public class MyController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "doSomething")
    public ResponseEntity doSomething() throws Exception{
        userService.doSomething();
        return new ResponseEntity(HttpStatus.OK);
    }
}
