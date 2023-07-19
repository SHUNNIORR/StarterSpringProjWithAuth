package com.shunnior.springsecurity.controllers;

import com.shunnior.springsecurity.controllers.request.CreateUserDto;
import com.shunnior.springsecurity.entities.RoleEntity;
import com.shunnior.springsecurity.entities.UserEntity;
import com.shunnior.springsecurity.repositories.UserRepository;
import com.shunnior.springsecurity.utils.ERole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class DemoController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRegistry sessionRegistry;
    @GetMapping("/demo")
    public String showHome() {
        return "Hello World!";
    }

    @GetMapping("/demo2")
    public String showDemo2() {
        return "Hello World! no asegurado";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){
        String sessionId = "";
        User userObj = null;

        List<Object> sessions = sessionRegistry.getAllPrincipals();
        for(Object session:sessions){
            if (session instanceof User){
                userObj = (User) session;
            }

            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session,false);

            for (SessionInformation sessionInformation: sessionInformations){
                sessionId = sessionInformation.getSessionId();
            }
        }
        Map<String,Object> response = new HashMap<>();
        response.put("Response","HelloWorld");
        response.put("sessioId",sessionId);
        response.put("sessionUser",userObj);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto createUserDto){

        Set<RoleEntity> roles = createUserDto.getRoles().stream()
                .map(role-> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .email(createUserDto.getEmail())
                .roles(roles)
                .build();
        userRepository.save(userEntity);
       return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "se ha borrado el user con id".concat(id);
    }
}
