package com.example.springbootstarter.Configuration;

import com.example.springbootstarter.Filters.AuthenticationFilter;
import com.example.springbootstarter.Filters.AuthorizationFilter;
import com.example.springbootstarter.ServicesImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfiguration {
    private final UserDetailsService userDetailsService;
    private final UserServiceImpl userService;

    @Value("${cors.allowed.origins}")
    private String originAllowed;

    @Value("${public.urls}")
    private Set<String> publicUrls;

    @Value("${access.token.expiry.in.hour}")
    private String accessTokenExpiry;

    @Value("${security.secret}")
    private String secret;


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager myAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider::authenticate;
    }


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.logout(lOut->{
            lOut.logoutUrl("/user/logout").invalidateHttpSession(true)
                    .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));

            // Allow CORS for the logout URL
            lOut.addLogoutHandler((request, response, authentication) -> {
                response.addHeader("Access-Control-Allow-Origin", originAllowed);
                response.addHeader("Access-Control-Allow-Credentials", "true");
            });
        });

        // Stateless Session because of JWT
        http.sessionManagement(session->{
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        // Disabling it causes the h2 database to be viewed.
        http.headers(header->
                header.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                )
        );

        // permitting urls without any role
        http.authorizeHttpRequests(authorize->{
            authorize.requestMatchers("/h2-console/**").permitAll();
            authorize.requestMatchers("/user/signUp/**").permitAll();
            authorize.requestMatchers("/user/logout/**").permitAll();
        });

        // permitting urls with role-based access
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/doSomething/**").hasAuthority("USER");
        });

        // authenticating any other url.
        http.authorizeHttpRequests(authorize->{
            authorize.anyRequest().authenticated();
        });

        // Enabling custom filters to act before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(new AuthorizationFilter(originAllowed, publicUrls, secret), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new AuthenticationFilter(authenticationManager, userService, accessTokenExpiry, secret), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

