package com.example.demo.configuration;

import com.example.demo.services.impl.PhotoContestUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
public class MVCSecurityConfig {


    private final String rememberMeKey;
    private final PasswordEncoder passwordEncoder;
    private final PhotoContestUserDetails photoContestUserDetails;
    private final CustomEntryPoint customEntryPoint;

    @Autowired
    public MVCSecurityConfig(@Value("${photo.remember.me.key}") String rememberMeKey,
                             PasswordEncoder passwordEncoder,
                             PhotoContestUserDetails photoContestUserDetails,
                             CustomEntryPoint customEntryPoint) {

        this.rememberMeKey = rememberMeKey;
        this.passwordEncoder = passwordEncoder;
        this.photoContestUserDetails = photoContestUserDetails;
        this.customEntryPoint = customEntryPoint;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
                    auth.requestMatchers("/", "/home", "/home/**", "/login", "/register", "/errors/**").permitAll();
                    auth.anyRequest().authenticated();

                })
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .failureHandler(failureHandler())
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                )
                .rememberMe(
                        rememberMe -> {
                            rememberMe.key(rememberMeKey)
                                      .userDetailsService(photoContestUserDetails)
                                      .rememberMeParameter("rememberme")
                                      .rememberMeCookieName("rememberme");
                        }
                )
                .logout(
                        logout -> {
                            logout.logoutUrl("/logout")
                                  .logoutSuccessUrl("/home")
                                  .invalidateHttpSession(true)
                                  .clearAuthentication(true)
                                  .permitAll();
                        })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customEntryPoint))


                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );


        return http.build();
    }




    @Bean("mvcAuthenticationManager")
    public AuthenticationManager mvcAuthenticationManager(HttpSecurity http) throws Exception {
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
