package io.academia.authentication.service;

import io.academia.authentication.dto.request.AccountRequest;
import io.academia.authentication.dto.response.AccountResponse;
import io.academia.authentication.model.Role;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);

    AccountResponse getAccountById(UUID id);

    List<AccountResponse> getAllAccounts();

    AccountResponse updateAccount(UUID id, AccountRequest accountRequest);

    AccountResponse updateRole(UUID id, Role newRole);

    AccountResponse changePassword(UUID id, String newPassword);

    void deleteAccount(UUID id);
}
