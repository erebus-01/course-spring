package io.academia.student.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.academia.student.model.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {

    private UUID id;
    private AddressResponse address;
    private String linksYourWebsite;
    private String linksYourFacebook;
    private String linksYourTwitter;
    private String linksYourLinkedIn;
    private String linksYourYoutube;
    private String urlPhoto;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
