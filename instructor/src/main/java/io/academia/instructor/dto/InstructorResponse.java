package io.academia.instructor.dto;

import io.academia.instructor.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstructorResponse {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String bio;
    private String profilePicture;
    private Status status;
    private String telephone;
    private String degree;
    private String story;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
