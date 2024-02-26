package io.academia.student.service;

import io.academia.student.dto.StudentRequest;
import io.academia.student.dto.StudentResponse;
import io.academia.student.model.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface StudentService {
    StudentResponse createStudent(StudentRequest student, MultipartFile mainImage) throws IOException;
    StudentResponse updateStudent(UUID studentId, StudentRequest student, MultipartFile mainImage) throws IOException;
    void deleteStudent(UUID studentId);
    StudentResponse getStudentById(UUID studentId);
    Student findByNamePhoto(String namePhoto);
}
