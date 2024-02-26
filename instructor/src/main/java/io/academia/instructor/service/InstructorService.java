package io.academia.instructor.service;

import io.academia.instructor.dto.InstructorRequest;
import io.academia.instructor.dto.InstructorResponse;

import java.util.List;
import java.util.UUID;

public interface InstructorService {
    InstructorResponse createInstructor(InstructorRequest request);
    InstructorResponse getInstructorById(UUID id);
    List<InstructorResponse> getAllInstructors();
    void deleteInstructorById(UUID id);
    InstructorResponse updateInstructor(UUID id, InstructorRequest request);
}
