package io.academia.authentication.service;

import io.academia.authentication.dto.request.AccountRequest;
import io.academia.authentication.dto.request.AuthenticationRequest;
import io.academia.authentication.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authentication(AuthenticationRequest request);
    AuthenticationResponse register(AccountRequest request);


}
