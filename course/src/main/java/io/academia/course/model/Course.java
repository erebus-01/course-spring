package io.academia.course.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(CourseListener.class)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true)
    @NotBlank
    @Size(min = 10, max = 200, message = "The name needs to be at least 10 characters and maximum 200 characters")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @JsonIgnore
    @Column(name = "main_image", columnDefinition = "bytea")
    private byte[] mainImage;

    private String nameImage;
    private String urlImage;
    private String typeImage;

    @Column(name = "size_image")
    private long sizeImage;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column(name = "price")
    private Double price;

    @Column(name = "instructor_id")
    private UUID instructorId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SlideImage> slideImages;

    @ElementCollection
    @CollectionTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id")
    )
    @Column(name = "student_id")
    private List<UUID> students;

    @ManyToMany
    @JoinTable(
            name = "course_category",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Section> sections;

    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;

}
