package com.lp3.elearning.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.CategoryResponseDTO;
import com.lp3.elearning.entities.Category;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CategoriesRepository;

import io.micrometer.common.lang.NonNull;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Category findCategoryEntityById(@NonNull Long id) {
        return categoriesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    // public CategoryResponse findCategoryById(@NonNull Long id) {
    //     Category category = findCategoryEntityById(id);
    //     return new CategoryResponse(category.getId(), category.getName());
    // }

    public Set<Category> getCategoriesByValidIds(Set<Long> ids) {
        
        List<Long> requestedIds = ids.stream().toList(); 
        
        List<Category> foundCategories = categoriesRepository.findAllById(requestedIds);

        if (foundCategories.size() != requestedIds.size()) {
            
            Set<Long> foundIds = foundCategories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());

            String missingIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

            throw new ResourceNotFoundException("As seguintes IDs de Categoria não foram encontradas: " + missingIds);
        }

        return new HashSet<>(foundCategories);
    }

    public Set<CategoryResponseDTO> toResponseDTOs(Set<Category> categories) {
        return categories.stream()
            .map(category -> new CategoryResponseDTO(category.getId(), category.getName()))
            .collect(Collectors.toSet());
    }   

    public Set<Category> toEntitys(Set<Long> ids) {
        Set<Category> categories = new HashSet<>();
        for (Long id : ids) {
            categories.add(findCategoryEntityById(id));
        }
        return categories;
    }

    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}