package io.academia.authentication.controller;

import io.academia.authentication.dto.request.AccountRequest;
import io.academia.authentication.dto.response.AccountResponse;
import io.academia.authentication.model.Role;
import io.academia.authentication.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        AccountResponse createdAccount = accountService.createAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable UUID id) {
        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable UUID id, @RequestBody AccountRequest accountRequest) {
        AccountResponse updatedAccount = accountService.updateAccount(id, accountRequest);
        return ResponseEntity.ok(updatedAccount);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<AccountResponse> updateRole(@PathVariable UUID id, @RequestParam Role newRole) {
        AccountResponse updatedAccount = accountService.updateRole(id, newRole);
        return ResponseEntity.ok(updatedAccount);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<AccountResponse> changePassword(@PathVariable UUID id, @RequestParam String newPassword) {
        AccountResponse updatedAccount = accountService.changePassword(id, newPassword);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
