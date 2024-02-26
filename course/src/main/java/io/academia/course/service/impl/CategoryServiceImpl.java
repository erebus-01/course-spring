package io.academia.course.service.impl;

import io.academia.course.dto.request.CategoryRequest;
import io.academia.course.dto.response.CategoryResponse;
import io.academia.course.exceptions.GlobalExceptionsHandler;
import io.academia.course.exceptions.ResourceNotFoundException;
import io.academia.course.model.Category;
import io.academia.course.repository.CategoryRepository;
import io.academia.course.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final GlobalExceptionsHandler exceptionsHandler;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id : " + id));
        return convertToResponseDto(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());

        categoryRepository.save(category);

        return convertToResponseDto(category);
    }

    @Override
    public List<CategoryResponse> createListCategories(List<CategoryRequest> categories) {
        List<CategoryResponse> responses = new ArrayList<>();
        for (CategoryRequest request : categories) {
            Category category = new Category();
            category.setName(request.getName());

            categoryRepository.save(category);
            responses.add(convertToResponseDto(category));
        }
        return responses;
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(request.getName());
                    existingCategory.setCourses(request.getCourses());
                    existingCategory = categoryRepository.save(existingCategory);
                    return convertToResponseDto(existingCategory);
                })
                .orElse(null);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


    private CategoryResponse convertToResponseDto(Category category) {
        return CategoryResponse.builder()
                .name(category.getName())
                .build();
    }

}
