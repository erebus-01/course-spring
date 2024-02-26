package io.academia.authentication.controller;

import io.academia.authentication.dto.request.AccountRequest;
import io.academia.authentication.dto.request.AuthenticationRequest;
import io.academia.authentication.dto.response.AuthenticationResponse;
import io.academia.authentication.service.AccountService;
import io.academia.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authentication(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AccountRequest request){
        return ResponseEntity.ok(service.register(request));
    }

}
