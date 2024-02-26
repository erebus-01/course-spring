package io.academia.authentication.service.impl;

import io.academia.authentication.dto.request.AccountRequest;
import io.academia.authentication.dto.request.AuthenticationRequest;
import io.academia.authentication.dto.response.AccountResponse;
import io.academia.authentication.dto.response.AuthenticationResponse;
import io.academia.authentication.model.Account;
import io.academia.authentication.model.Role;
import io.academia.authentication.repository.AccountRepository;
import io.academia.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository repository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        log.info("Ready login with email: {} and password: {}", request.getEmail(), request.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if(user.isVerified()) {
            log.info("User {} logged in to the system", user);
            var jwtToken = jwtService.generatorToken(user);

            return AuthenticationResponse.builder().token(jwtToken).build();
        }

        return null;
    }

    @Override
    public AuthenticationResponse register(AccountRequest accountRequest) {
        Set<Role> roles = Set.of(Role.ROLE_USER);
        Account account = Account.builder()
                .email(accountRequest.getEmail())
                .firstName(accountRequest.getFirstName())
                .lastName(accountRequest.getLastName())
                .phoneNumber(accountRequest.getPhoneNumber())
                .gender(accountRequest.getGender())
                .dateOfBirth(accountRequest.getDateOfBirth())
                .role(accountRequest.getRole() != null ? accountRequest.getRole() : roles)
                .password(passwordEncoder.encode(accountRequest.getPassword()))
                .build();

        Account savedAccount = repository.save(account);

        var jwtToken = jwtService.generatorToken(savedAccount);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
