package com.mallquidev.expense_tracker_api.service;

import com.mallquidev.expense_tracker_api.dto.category.CategoryDto;
import com.mallquidev.expense_tracker_api.entities.Category;
import com.mallquidev.expense_tracker_api.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //listar toda las categorias
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    //busca una categoria especifica
    public Optional<CategoryDto> getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(category -> new CategoryDto(category.getId(), category.getName()));
    }

    //registrar
    public CategoryDto registerCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());

        Category savedCategory = categoryRepository.save(category);
        return new CategoryDto(savedCategory.getId(), savedCategory.getName());
    }

    //delete category
    public void deleteCategoryById(Integer id) {
        if(!categoryRepository.existsById(id)){
            throw new IllegalStateException("Category does not exist");
        }
        categoryRepository.deleteById(id);
    }

    //update category
    public CategoryDto updateCategory(Integer id, CategoryDto categoryDto) {
        //verificamos si existe
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Category does not exist"));

        category.setName(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDto(savedCategory.getId(), savedCategory.getName());
    }



}
