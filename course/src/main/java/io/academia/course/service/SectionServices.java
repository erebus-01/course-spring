package io.academia.course.service;

import io.academia.course.dto.request.SectionRequest;
import io.academia.course.dto.response.SectionResponse;

import java.util.List;
import java.util.UUID;

public interface SectionServices {
    SectionResponse createSection(SectionRequest sectionRequest);
    SectionResponse getSectionById(UUID sectionId);
    List<SectionResponse> getAllSections();
    SectionResponse updateSection(UUID sectionId, SectionRequest updatedSectionRequest);
    void deleteSection(UUID sectionId);
}
