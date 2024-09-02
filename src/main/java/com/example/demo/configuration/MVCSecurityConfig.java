package com.example.demo.configuration;

import com.example.demo.services.UserService;
import com.example.demo.services.impl.PhotoContestUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class MVCSecurityConfig {


    private final String rememberMeKey;
    private final PasswordEncoder passwordEncoder;
    private final PhotoContestUserDetails photoContestUserDetails;
    private final CustomEntryPoint customEntryPoint;

    @Autowired
    public MVCSecurityConfig(@Value("${forum.remember.me.key}") String rememberMeKey,
                             PasswordEncoder passwordEncoder,
                             PhotoContestUserDetails photoContestUserDetails,
                             CustomEntryPoint customEntryPoint) {

        this.rememberMeKey = rememberMeKey;
        this.passwordEncoder = passwordEncoder;
        this.photoContestUserDetails = photoContestUserDetails;
        this.customEntryPoint = customEntryPoint;
    }


    @Bean("mvcAuthenticationManager")
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(photoContestUserDetails)
               .passwordEncoder(passwordEncoder);
        return builder.build();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
        handler.setDefaultFailureUrl("/login?error=true");
        return handler;
    }


    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/home");
        return handler;
    }


}
