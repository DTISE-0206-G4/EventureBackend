package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.model.Category;
import com.GrAsp.EventureBackend.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
