package com.ecm.services.category;

import com.ecm.dtos.CategoryDTO;
import com.ecm.models.Category;
import com.ecm.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("not found"));
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) throws Exception {
        Category existingCategory = getCategoryById(id);
        Category updateCategory = Category.builder()
                .name(existingCategory.getName())
                .build();
        return categoryRepository.save(updateCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
