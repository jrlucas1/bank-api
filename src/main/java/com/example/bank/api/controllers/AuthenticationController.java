package com.example.bank.api.controllers;

import com.example.bank.domain.user.AuthenticationDTO;
import com.example.bank.domain.user.LoginResponseDTO;
import com.example.bank.domain.user.RegisterDTO;
import com.example.bank.domain.user.User;
import com.example.bank.infra.security.TokenService;
import com.example.bank.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository rep;

    @Autowired
    private TokenService service;


    @PostMapping("/login")
    public ResponseEntity login (@RequestBody @Valid AuthenticationDTO login){
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.login(), login.password());
        var auth = this.manager.authenticate(usernamePassword);

        var token = service.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid RegisterDTO login){
        if(this.rep.findByLogin(login.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(login.password());
        User newUser = new User(login.login(), encryptedPassword, login.role());

        this.rep.save(newUser);

        return ResponseEntity.ok().build();
    }

}