package io.academia.student.repository;

import io.academia.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Student findByNamePhoto(String namePhoto);
}
