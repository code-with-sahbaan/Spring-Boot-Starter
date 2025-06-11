package com.example.springbootstarter.Utils;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.springbootstarter.ResponseDTO.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FilterUtil {

    public static void writeErrorResponse(HttpServletResponse response, String message, Integer status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseMessage(message);
        new ObjectMapper().writeValue(response.getOutputStream(), baseResponse);
    }
}
