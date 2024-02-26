package io.academia.authentication.dto.response;

import io.academia.authentication.model.Gender;
import io.academia.authentication.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isActive;
    private boolean isVerified;
    private Gender gender;
    private Set<Role> role;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
