package io.academia.course.service.impl;

import io.academia.course.dto.response.LessonResponse;
import io.academia.course.dto.request.SectionRequest;
import io.academia.course.dto.response.SectionResponse;
import io.academia.course.model.Lesson;
import io.academia.course.model.Section;
import io.academia.course.repository.SectionRepository;
import io.academia.course.service.LessonServices;
import io.academia.course.service.SectionServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SectionServicesImpl implements SectionServices {

    private final SectionRepository sectionRepository;
    private final LessonServices lessonServices;

    @Override
    public SectionResponse createSection(SectionRequest sectionRequest) {
        Section section = mapRequestToSection(sectionRequest);
        Section savedSection = sectionRepository.save(section);
        return mapSectionToResponse(savedSection);
    }

    @Override
    public SectionResponse getSectionById(UUID sectionId) {
        Optional<Section> sectionOptional = sectionRepository.findById(sectionId);
        if(sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            return mapSectionToResponse(section);
        }
        return null;
    }

    @Override
    public List<SectionResponse> getAllSections() {
        List<Section> sections = sectionRepository.findAll();
        return sections.stream()
                .map(this::mapSectionToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SectionResponse updateSection(UUID sectionId, SectionRequest updatedSectionRequest) {
        Section existingSection = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found with id: " + sectionId));

        Section updatedSection = mapRequestToExistingSection(updatedSectionRequest, existingSection);
        Section savedSection = sectionRepository.save(updatedSection);
        return mapSectionToResponse(savedSection);
    }

    @Override
    public void deleteSection(UUID sectionId) {
        sectionRepository.deleteById(sectionId);

    }

    private List<LessonResponse> convertToLesson(UUID sectionId) {
        return lessonServices.getAllLessons(sectionId);
    }

    private Lesson mapToLesson(LessonResponse request) {
        return Lesson.builder()
                .url(request.getUrl())
                .content(request.getContent())
                .title(request.getTitle())
                .build();
    }

    private Section mapRequestToSection(SectionRequest sectionRequest) {

        Section section = new Section();

        section.setTitle(sectionRequest.getTitle());
        section.setCourse(sectionRequest.getCourse());

        List<Lesson> lessonList = new ArrayList<>();
        
        for(Lesson lesson1 : sectionRequest.getLessons()) {
            Lesson lesson = new Lesson();
            lesson.setTitle(lesson1.getTitle());
            lesson.setUrl(lesson1.getUrl());
            lesson.setContent(lesson1.getContent());
            lesson.setSession(lesson1.getSession());
            lesson.setSession(section);

            lessonList.add(lesson);
        }

        section.setLessons(lessonList);
        
        return section;
    }

    private SectionResponse mapSectionToResponse(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .lessons(convertToLesson(section.getId()))
                .build();
    }

    private Section mapRequestToExistingSection(SectionRequest updatedSectionRequest, Section existingSection) {
        existingSection.setTitle(updatedSectionRequest.getTitle());
        existingSection.setLessons(updatedSectionRequest.getLessons());
        return existingSection;
    }
}
