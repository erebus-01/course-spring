package io.academia.course.controller;

import io.academia.course.dto.request.SectionRequest;
import io.academia.course.dto.response.SectionResponse;
import io.academia.course.service.SectionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/section")
public class SectionController {

    private final SectionServices sectionServices;

    @PostMapping()
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionRequest sectionRequest) {
        SectionResponse createdSection = sectionServices.createSection(sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSection);
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> getSectionById(@PathVariable("sectionId") UUID sectionId) {
        SectionResponse section = sectionServices.getSectionById(sectionId);
        return ResponseEntity.ok(section);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SectionResponse>> getAllSections() {
        List<SectionResponse> sections = sectionServices.getAllSections();
        return ResponseEntity.ok(sections);
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> updateSection(
            @PathVariable UUID sectionId,
            @RequestBody SectionRequest updatedSectionRequest) {
        SectionResponse updatedSection = sectionServices.updateSection(sectionId, updatedSectionRequest);
        return ResponseEntity.ok(updatedSection);
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSection(@PathVariable UUID sectionId) {
        sectionServices.deleteSection(sectionId);
        return ResponseEntity.noContent().build();
    }

}
