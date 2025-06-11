package com.example.springbootstarter.Filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springbootstarter.ResponseDTO.BaseResponse;
import com.example.springbootstarter.Utils.FilterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static java.util.Arrays.stream;

public class AuthorizationFilter extends OncePerRequestFilter {

    private final String originAllowed;

    private final Set<String> publicURLs;

    public final String secret;

    public AuthorizationFilter(String originAllowed, Set<String> publicURLs, String secret) {
        this.originAllowed = originAllowed;
        this.publicURLs = publicURLs;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Allow pre-flight (OPTIONS) requests to pass through without auth
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        /**
         * @Cors Policies
         */

        response.setHeader("Access-Control-Allow-Origin", originAllowed);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setIntHeader("Access-Control-Max-Age", 3600);

        /**
         * @Request Handling
         */

        // handling if it is non-tokenized url i.e public url
        if (publicURLs.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            // or if it is tokenized url then checking the url in header and checking it.
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);
                    String username = decodedJWT.getSubject();

                    // Check user not already authenticated
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        stream(roles).forEach(role->{
                            authorities.add(new SimpleGrantedAuthority(role));
                        });
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    // if authorization fails returning with error
                    FilterUtil.writeErrorResponse(response,
                            e.getCause().getMessage(),
                            e instanceof TokenExpiredException ? HttpServletResponse.SC_FORBIDDEN : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

            }else{
                // If the url is tokenized and token is not available then responding with error
                FilterUtil.writeErrorResponse(response,
                        "Un-Authorized Access",
                        HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }
}
