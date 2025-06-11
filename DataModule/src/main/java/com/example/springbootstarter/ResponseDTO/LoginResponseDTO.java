package com.example.springbootstarter.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private long userId;
    private String email;
    private String fullName;
    private String accessToken;
}
