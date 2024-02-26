package io.academia.instructor.controller;

import io.academia.instructor.dto.InstructorRequest;
import io.academia.instructor.dto.InstructorResponse;
import io.academia.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    public ResponseEntity<InstructorResponse> createInstructor(@RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.createInstructor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponse> getInstructorById(@PathVariable UUID id) {
        InstructorResponse response = instructorService.getInstructorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InstructorResponse>> getAllInstructors() {
        List<InstructorResponse> response = instructorService.getAllInstructors();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructorById(@PathVariable UUID id) {
        instructorService.deleteInstructorById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponse> updateInstructor(@PathVariable UUID id, @RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.updateInstructor(id, request);
        return ResponseEntity.ok(response);
    }

}
