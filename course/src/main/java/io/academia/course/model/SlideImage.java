package io.academia.course.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "slide_image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

    @Column(name = "image_type")
    private String imageType;

    private String imageName;
    private String imageUri;

    @Column(name = "image_size")
    private long imageSize;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Course course;

}
