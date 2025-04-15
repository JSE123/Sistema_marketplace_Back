package com.marketplace.MarketBack.controller;

import com.marketplace.MarketBack.controller.dto.AuthCreateUserRequest;
import com.marketplace.MarketBack.controller.dto.AuthLoginRequest;
import com.marketplace.MarketBack.controller.dto.AuthResponse;
import com.marketplace.MarketBack.service.UserDetailServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @GetMapping("/prueba")
    public String prueba(){
        return "hola";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest){
        return new ResponseEntity<>(this.userDetailServiceImp.createUser(userRequest), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailServiceImp.loginUser(userRequest), HttpStatus.OK);
    }

}
