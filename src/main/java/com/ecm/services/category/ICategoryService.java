package com.ecm.services.category;

import com.ecm.dtos.CategoryDTO;
import com.ecm.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    List<Category> getAllCategories();
    Category getCategoryById(Long id) throws Exception;
    Category updateCategory(Long id, CategoryDTO categoryDTO) throws Exception;
    void deleteCategory(Long id);
}
