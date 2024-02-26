package io.academia.student.controller;

import io.academia.student.dto.StudentRequest;
import io.academia.student.dto.StudentResponse;
import io.academia.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@ModelAttribute StudentRequest studentRequest,
                                                         @RequestParam("photo") MultipartFile mainImage) throws IOException {
        StudentResponse studentResponse = studentService.createStudent(studentRequest, mainImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentResponse);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable UUID studentId,
                                                         @ModelAttribute StudentRequest studentRequest,
                                                         @RequestParam("photo") MultipartFile mainImage) throws IOException {
        StudentResponse studentResponse = studentService.updateStudent(studentId, studentRequest, mainImage);
        return ResponseEntity.ok(studentResponse);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable UUID studentId) {
        StudentResponse studentResponse = studentService.getStudentById(studentId);
        return ResponseEntity.ok(studentResponse);
    }

}
