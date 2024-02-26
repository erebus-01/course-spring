package io.academia.instructor.service.impl;

import io.academia.instructor.dto.InstructorRequest;
import io.academia.instructor.dto.InstructorResponse;
import io.academia.instructor.model.Instructor;
import io.academia.instructor.model.Status;
import io.academia.instructor.repository.InstructorRepository;
import io.academia.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    @Override
    public InstructorResponse createInstructor(InstructorRequest request) {
        Instructor instructor = new Instructor();
        return getInstructorResponse(request, instructor);
    }

    @Override
    public InstructorResponse getInstructorById(UUID id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow();
        return convertToDTO(instructor);    }

    @Override
    public List<InstructorResponse> getAllInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        return instructors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInstructorById(UUID id) {
        instructorRepository.deleteById(id);
    }

    @Override
    public InstructorResponse updateInstructor(UUID id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow();

        return getInstructorResponse(request, instructor);
    }

    private InstructorResponse getInstructorResponse(InstructorRequest request, Instructor instructor) {
        instructor.setFirstname(request.getFirstname());
        instructor.setLastname(request.getLastname());
        instructor.setEmail(request.getEmail());
        instructor.setBio(request.getBio());
        instructor.setProfilePicture(request.getProfilePicture());
        instructor.setStatus(request.getStatus());
        instructor.setTelephone(request.getTelephone());
        instructor.setDegree(request.getDegree());
        instructor.setStory(request.getStory());

        Instructor updatedInstructor = instructorRepository.save(instructor);

        return convertToDTO(updatedInstructor);
    }

    private InstructorResponse convertToDTO(Instructor instructor) {
        InstructorResponse dto = new InstructorResponse();
        dto.setId(instructor.getId());
        dto.setFirstname(instructor.getFirstname());
        dto.setLastname(instructor.getLastname());
        dto.setEmail(instructor.getEmail());
        dto.setBio(instructor.getBio());
        dto.setProfilePicture(instructor.getProfilePicture());
        dto.setStatus(instructor.getStatus());
        dto.setTelephone(instructor.getTelephone());
        dto.setDegree(instructor.getDegree());
        dto.setStory(instructor.getStory());
        dto.setCreateAt(instructor.getCreateAt());
        dto.setUpdateAt(instructor.getUpdateAt());
        return dto;
    }
}
