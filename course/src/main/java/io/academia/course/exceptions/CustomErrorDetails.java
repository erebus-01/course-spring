package io.academia.course.exceptions;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorDetails {

    private Date timestamp;
    private String message;
    private String status;
    private Boolean success;
    private String errors;

}
