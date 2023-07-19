package com.shunnior.springsecurity.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {
    @GetMapping("/accesAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin(){
        return "Hola Admin";
    }
    @GetMapping("/accesUser")
    @PreAuthorize("hasRole('USER')")
    public String accesUser(){
        return "Hola User";
    }

    @GetMapping("/accesInvited")
    @PreAuthorize("hasRole('USER')")
    public String accesInvited(){
        return "Hola Invited";
    }
}
