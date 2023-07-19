package com.shunnior.springsecurity.config;

import com.shunnior.springsecurity.config.filters.JwtAuthenticationFilter;
import com.shunnior.springsecurity.config.filters.JwtAuthorizationFilter;
import com.shunnior.springsecurity.services.UserDetailsImpl;
import com.shunnior.springsecurity.config.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    UserDetailsImpl userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        var user = User.withUsername("uncledave")
//                .password("password123")
//                .roles("read")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
    /*@Bean
    SecurityUserDetailsService userDetailsService(){
        return new SecurityUserDetailsService();
    }*/
   /* @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance(); //No apto para producción
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
*/
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        return http.
//                authorizeRequests()
//                    .requestMatchers("/createUser").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                    .successHandler(successHandler())
//                    .permitAll()
//                .and()
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                    .invalidSessionUrl("/login")
//                    .maximumSessions(1)
//                    .expiredUrl("/login")
//                .and()
//                .sessionFixation()
//                .migrateSession()
//                .and()
//                .build();
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager)throws Exception{
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return httpSecurity
                .csrf(config-> config.disable())
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers("/demo").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder).
                and().build();
    }

}

