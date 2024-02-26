package io.academia.course.service;

import io.academia.course.dto.request.CategoryRequest;
import io.academia.course.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryRequest category);
    List<CategoryResponse> createListCategories(List<CategoryRequest> categories);
    CategoryResponse updateCategory(Long id, CategoryRequest category);
    void deleteCategory(Long id);

}
