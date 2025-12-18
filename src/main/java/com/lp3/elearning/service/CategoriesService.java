package com.lp3.elearning.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.CategoryRequestDTO;
import com.lp3.elearning.dto.CategoryResponseDTO;
import com.lp3.elearning.entities.Category;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CategoriesRepository;
import com.lp3.elearning.repository.CourseRepository;
import io.micrometer.common.lang.NonNull;

@Service
public class CategoriesService {

    private final CourseRepository courseRepository;

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository, CourseRepository courseRepository) {
        this.categoriesRepository = categoriesRepository;
        this.courseRepository = courseRepository;
    }
    
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = Category.builder()
                .name(dto.name())
                .build();
        
        category = categoriesRepository.save(category);
        return toResponseDTO(category);
    }
    
    public List<CategoryResponseDTO> findAll() {
        return categoriesRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public CategoryResponseDTO findById(Long id) {
        return toResponseDTO(findCategoryEntityById(id));
    }


    public Category findCategoryEntityById(@NonNull Long id) {
        return categoriesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    public Set<Category> getCategoriesByValidIds(Set<Long> ids) {
            if (ids == null || ids.isEmpty()) {
                return new HashSet<>();
            }

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

    // Adicionar Update
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = findCategoryEntityById(id);
        category.setName(dto.name());
        return toResponseDTO(categoriesRepository.save(category));
    }

    // Adicionar Delete
    public void delete(Long id) {
        if (!categoriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada.");
        }
        // Opcional: Verificar se existem cursos vinculados antes de deletar
        if (courseRepository.existsByCategoryId(id)) { 
            throw new ConflictException("a categoria que c ta tentando deletar tem curso ligada nela"); 
        }
        
        categoriesRepository.deleteById(id);
    }
}