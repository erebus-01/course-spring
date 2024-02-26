package io.academia.student.dto;

import io.academia.student.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {

    private String streetAddress;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String recipientName;
    private Student student;

}
