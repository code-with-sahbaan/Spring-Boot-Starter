package com.example.springbootstarter.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "USERS")
@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "EMAIL",
            unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FULL_NAME")
    private String fullName;

}
