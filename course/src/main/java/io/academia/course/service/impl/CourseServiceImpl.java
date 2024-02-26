package io.academia.course.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.academia.course.dto.request.CourseRequest;
import io.academia.course.dto.request.CourseRequestUpdate;
import io.academia.course.dto.response.*;
import io.academia.course.model.*;
import io.academia.course.repository.CourseRepository;
import io.academia.course.service.CourseRedisService;
import io.academia.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final WebClient webClient;
    private final CourseRedisService redisService;

    @Override
    public CourseResponse createCourse(CourseRequest courseRequest, MultipartFile mainImage, MultipartFile[] slideImage) throws IOException {
        Course course = mapRequestToCourse(courseRequest, mainImage, slideImage);
        course = courseRepository.save(course);
        return mapToCourseResponse(course);
    }

    @Override
    public CourseResponse getCourseById(UUID courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        return optionalCourse.map(course -> {
            CourseResponse courseResponse = mapToCourseResponse(course);

            List<Category> categories = course.getCategories();
            List<Section> sections = course.getSections();
            List<SlideImage> slideImages = course.getSlideImages();

            List<CategoryResponse> categoryResponses = mapCategoriesToDto(categories);
            List<SectionResponse> sectionResponses = mapSectionsToDto(sections);

            courseResponse.setCategories(categoryResponses);
            courseResponse.setSections(sectionResponses);

            List<SlideImageDto> slideImageResponse = mapSlideImagesToResponse(slideImages);
            courseResponse.setSlideImages(slideImageResponse);

            return courseResponse;
        }).orElse(null);
    }

    @Override
    public Course getOneById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElse(null);
    }

    @Override
    public Page<CourseResponse> findAll(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(this::mapToCourseResponseGetAll);
    }

    @Override
    public List<CourseResponse> importCoursesFromExcel(MultipartFile excel) throws IOException {
        List<Course> courseResponses = parseExcel(excel);
        List<Course> save = courseRepository.saveAll(courseResponses);
        return save.stream().map(this::mapToCourseResponseGetAll).collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> importCoursesFromCsv(MultipartFile csv) throws IOException {
        List<Course> courseResponses = parseCsv(csv);
        List<Course> save = courseRepository.saveAll(courseResponses);
        return save.stream().map(this::mapToCourseResponseGetAll).collect(Collectors.toList());
    }

    @Override
    public Page<CourseResponse> searchCourses(String keyword, List<Long> categoryIds, int page, int size) throws JsonProcessingException {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<CourseResponse> courseRedisPage = redisService.getAllCourse(keyword, categoryIds, pageable);

        if(courseRedisPage == null) {
            Page<Course> courseResponses;
            if (categoryIds != null && !categoryIds.isEmpty()) {
                courseResponses = courseRepository.findByCategoriesIdInAndNameContainingIgnoreCase(categoryIds, keyword, pageable);
            } else {
                courseResponses = courseRepository.findByNameContainingIgnoreCase(keyword, pageable);
            }

            Page<CourseResponse> courseResponsePage = courseResponses.map(this::mapToCourseResponseGetAll);
            redisService.saveAllCourse(courseResponsePage, keyword, categoryIds, pageable);

            return courseResponsePage;
        }

        return courseRedisPage;
    }

    @Override
    public Course findByNameImage(String nameImage) {
        return courseRepository.findByNameImage(nameImage);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToCourseResponseGetAll)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(UUID courseId, CourseRequestUpdate updatedCourseRequest) {
        return courseRepository.findById(courseId)
                .map(existingCourse -> {
                        mapUpdatedRequestToCourse(updatedCourseRequest, existingCourse);
                    existingCourse = courseRepository.save(existingCourse);
                    return mapToCourseResponse(existingCourse);
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public CourseResponse updateImageCourse(UUID courseId, MultipartFile mainImage) throws IOException {
        Course course = courseRepository.findById(courseId).orElseThrow();

        InsertMainImage(mainImage, course);
        courseRepository.save(course);

        return mapToCourseResponse(course);
    }

    @Override
    public List<SlideImageDto> updateImageSlide(UUID courseId, MultipartFile[] slideImage) throws IOException {
        Course course = courseRepository.findById(courseId).orElseThrow();

        List<SlideImage> slideImages = InsertSlideImage(slideImage, course);
        return mapSlideImagesToResponse(slideImages);
    }

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<CourseResponse> findCoursesByIds(List<UUID> courseIds) {
        Thread.sleep(1000);
        List<Course> courses = courseRepository.findAllByIdIn(courseIds);
        return courses.stream().map(this::mapToCourseResponseGetAll).collect(Collectors.toList());
    }

    private List<SlideImage> InsertSlideImage(MultipartFile[] slideImage, Course course) throws IOException {
        if (slideImage != null) {
            List<SlideImage> slideImages = new ArrayList<>();
            for (MultipartFile slideImageFile : slideImage) {
                SlideImage slide = new SlideImage();
                String newName = RenameFileImage(slideImageFile);
                String fileDownloadUri = "http://localhost:8500/file-image/" + newName;

                slide.setCourse(course);
                slide.setImageData(slideImageFile.getBytes());
                slide.setImageType(slideImageFile.getContentType());
                slide.setImageSize(slideImageFile.getSize());
                slide.setImageName(newName);
                slide.setImageUri(fileDownloadUri);

                slideImages.add(slide);
            }
            course.setSlideImages(slideImages);

            courseRepository.save(course);

            return slideImages;
        }
        return null;
    }


    private void InsertMainImage(MultipartFile mainImage, Course course) throws IOException {
        if (mainImage != null && !mainImage.isEmpty()) {

            String newName = RenameFileImage(mainImage);
            String fileDownloadUri = "http://localhost:8500/files/" + newName;

            course.setMainImage(mainImage.getBytes());
            course.setTypeImage(mainImage.getContentType());
            course.setNameImage(newName);
            course.setSizeImage(mainImage.getSize());
            course.setUrlImage(fileDownloadUri);
        }
    }

    @Override
    public void deleteCourse(UUID courseId) {
        courseRepository.deleteById(courseId);
    }

    private CourseResponse mapToCourseResponse(Course request) {
        return CourseResponse.builder()
                .id(request.getId())
                .description(request.getDescription())
                .price(request.getPrice())
                .instructor(request.getInstructorId())
                .difficultyLevel(request.getDifficultyLevel())
                .endDate(request.getEndDate())
                .nameImage(request.getNameImage())
                .sizeImage(request.getSizeImage())
                .typeImage(request.getTypeImage())
                .urlImage(request.getUrlImage())
                .name(request.getName())
                .sections(mapSectionsToDto(request.getSections()))
                .slideImages(mapSlideImagesToResponse(request.getSlideImages()))
                .startDate(request.getStartDate())
                .categories(mapCategoriesToDto(request.getCategories()))
                .updateAt(request.getUpdateAt())
                .createAt(request.getCreateAt())
                .build();
    }

    private Course mapRequestToCourse(CourseRequest courseRequest, MultipartFile mainImage, MultipartFile[] slideImage) throws IOException {
        Course course = new Course();


        InsertMainImage(mainImage, course);

        InsertSlideImage(slideImage, course);

        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        course.setStartDate(courseRequest.getStartDate());
        course.setEndDate(courseRequest.getEndDate());
        course.setDifficultyLevel(courseRequest.getDifficultyLevel());
        course.setPrice(courseRequest.getPrice());
        course.setSections(courseRequest.getSections());
        course.setInstructorId(courseRequest.getInstructor());
        course.setCategories(courseRequest.getCategories());

        return course;
    }

    private List<SlideImageDto> mapSlideImagesToResponse(List<SlideImage> slideImages) {
        List<SlideImageDto> slideImageResponses = new ArrayList<>();
        if (slideImages != null) {
            for (SlideImage slideImage : slideImages) {
                SlideImageDto slideImageResponse = SlideImageDto.builder()
                        .id(slideImage.getId())
                        .imageUri(slideImage.getImageUri())
                        .imageName(slideImage.getImageName())
                        .build();
                slideImageResponses.add(slideImageResponse);
            }
        }
        return slideImageResponses;
    }

    private List<CategoryResponse> mapCategoriesToDto(List<Category> categories) {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        if(categories != null) {
            for (Category category : categories) {
                CategoryResponse categoryDto = CategoryResponse.builder()
                        .name(category.getName())
                        .build();
                categoryResponses.add(categoryDto);
            }
        }
        return categoryResponses;
    }

    public List<SectionResponse> mapSectionsToDto(List<Section> sections) {
        List<SectionResponse> sectionResponses = new ArrayList<>();
        if(sections != null) {
            for (Section section : sections) {
                SectionResponse sectionDto = new SectionResponse();
                sectionDto.setId(section.getId());
                sectionDto.setTitle(section.getTitle());
                sectionDto.setLessons(mapLessonToResponse(section.getLessons()));
                sectionResponses.add(sectionDto);
            }
        }
        return sectionResponses;
    }

    private List<LessonResponse> mapLessonToResponse(List<Lesson> lessons) {
        List<LessonResponse> lessonResponses = new ArrayList<>();
        for (Lesson lesson : lessons) {
            LessonResponse lessonResponse = LessonResponse.builder()
                    .title(lesson.getTitle())
                    .content(lesson.getContent())
                    .url(lesson.getUrl())
                    .build();
            lessonResponses.add(lessonResponse);
        }

        return lessonResponses;
    }

    private CourseResponse mapToCourseResponseGetAll(Course request) {
        return CourseResponse.builder()
                .id(request.getId())
                .name(request.getName())
                .description(request.getDescription())
                .urlImage(request.getUrlImage())
                .price(request.getPrice())
                .difficultyLevel(request.getDifficultyLevel())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createAt(request.getCreateAt())
                .updateAt(request.getUpdateAt())
                .build();
    }

    private static String RenameFileImage(MultipartFile mainImage) {
        String randomFileName = UUID.randomUUID().toString();

        String originalFileName = mainImage.getOriginalFilename();
        assert originalFileName != null;
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        return randomFileName + fileExtension;
    }

    private void mapUpdatedRequestToCourse(CourseRequestUpdate updatedCourseRequest, Course existingCourse) {
        existingCourse.setName(updatedCourseRequest.getName());
        existingCourse.setDescription(updatedCourseRequest.getDescription());
        existingCourse.setStartDate(updatedCourseRequest.getStartDate());
        existingCourse.setEndDate(updatedCourseRequest.getEndDate());
        existingCourse.setDifficultyLevel(updatedCourseRequest.getDifficultyLevel());
        existingCourse.setPrice(updatedCourseRequest.getPrice());
        existingCourse.setCategories(updatedCourseRequest.getCategories());

    }

    private List<Course> parseExcel(MultipartFile excelFile) throws IOException {
        List<Course> courses = new ArrayList<>();
        try (InputStream inputStream = excelFile.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                log.info("It's row: {}", row);
                if (row.getRowNum() == 0) continue;
                Course course = new Course();
                course.setId(UUID.randomUUID());
                course.setName(row.getCell(0).getStringCellValue());
                course.setDescription(row.getCell(1).getStringCellValue());
                course.setStartDate(row.getCell(2).getDateCellValue());
                course.setEndDate(row.getCell(3).getDateCellValue());
                course.setDifficultyLevel(DifficultyLevel.valueOf(row.getCell(4).getStringCellValue()));
                course.setPrice(row.getCell(5).getNumericCellValue());

                courses.add(course);
            }
        }
        return courses;
    }


    private List<Course> parseCsv(MultipartFile csvFile) throws IOException {
        List<Course> courses = new ArrayList<>();
        try (Reader reader = new InputStreamReader(csvFile.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            String[] headers = csvReader.readNext();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                Course course = new Course();
                course.setName(line[0]);
                course.setDescription(line[1]);
                courses.add(course);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return courses;
    }

}
