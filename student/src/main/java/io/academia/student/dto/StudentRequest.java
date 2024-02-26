package io.academia.student.dto;

import io.academia.student.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {

    private UUID studentId;
    private Address address;
    private String linksYourWebsite;
    private String linksYourFacebook;
    private String linksYourTwitter;
    private String linksYourLinkedIn;
    private String linksYourYoutube;
    private MultipartFile photo;

}
