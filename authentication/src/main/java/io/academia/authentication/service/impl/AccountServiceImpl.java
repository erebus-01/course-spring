package io.academia.authentication.service.impl;

import io.academia.authentication.dto.request.AccountRequest;
import io.academia.authentication.dto.response.AccountResponse;
import io.academia.authentication.model.Account;
import io.academia.authentication.model.Role;
import io.academia.authentication.repository.AccountRepository;
import io.academia.authentication.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = Account.builder()
                .email(accountRequest.getEmail())
                .firstName(accountRequest.getFirstName())
                .lastName(accountRequest.getLastName())
                .phoneNumber(accountRequest.getPhoneNumber())
                .gender(accountRequest.getGender())
                .role(accountRequest.getRole())
                .password(accountRequest.getPassword())
                .build();
        Account savedAccount = accountRepository.save(account);
        return mapToResponse(savedAccount);
    }

    @Override
    public AccountResponse getAccountById(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow();
        return mapToResponse(account);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public AccountResponse updateAccount(UUID id, AccountRequest accountRequest) {
        Account existingAccount = accountRepository.findById(id).orElse(null);
        if (existingAccount != null) {
            existingAccount.setEmail(accountRequest.getEmail());
            existingAccount.setFirstName(accountRequest.getFirstName());
            existingAccount.setLastName(accountRequest.getLastName());
            existingAccount.setPhoneNumber(accountRequest.getPhoneNumber());
            existingAccount.setGender(accountRequest.getGender());
            Account updatedAccount = accountRepository.save(existingAccount);
            return mapToResponse(updatedAccount);
        }
        return null;
    }

    @Override
    public AccountResponse updateRole(UUID id, Role newRole) {
        Account existingAccount = accountRepository.findById(id).orElse(null);
        if (existingAccount != null) {
            existingAccount.setRole(Collections.singleton(newRole));
            Account updatedAccount = accountRepository.save(existingAccount);
            return mapToResponse(updatedAccount);
        }
        return null;
    }

    @Override
    public AccountResponse changePassword(UUID id, String newPassword) {
        Account existingAccount = accountRepository.findById(id).orElse(null);
        if (existingAccount != null) {
            existingAccount.setPassword(newPassword);
            Account updatedAccount = accountRepository.save(existingAccount);
            return mapToResponse(updatedAccount);
        }
        return null;
    }

    @Override
    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }


    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phoneNumber(account.getPhoneNumber())
                .isActive(account.isActive())
                .isVerified(account.isVerified())
                .gender(account.getGender())
                .role(account.getRole())
                .createAt(account.getCreateAt())
                .updateAt(account.getUpdateAt())
                .build();
    }


}
