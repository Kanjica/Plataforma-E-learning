package com.lp3.elearning.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.CategoryRequestDTO;
import com.lp3.elearning.dto.CategoryResponseDTO;
import com.lp3.elearning.entities.Category;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CategoriesRepository;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }
    
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = Category.builder().name(dto.name()).build();
        return toResponseDTO(categoriesRepository.save(category));
    }
    
    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = findCategoryEntityById(id);
        category.setName(dto.name());
        return toResponseDTO(categoriesRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        if (!categoriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada.");
        }
        categoriesRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoriesRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
        return toResponseDTO(findCategoryEntityById(id));
    }

    public Category findCategoryEntityById(Long id) {
        return categoriesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    /**
     * Valida uma lista de IDs e retorna as entidades correspondentes.
     * @throws ResourceNotFoundException se algum ID não existir.
     */
    public Set<Category> getCategoriesByValidIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        
        List<Category> foundCategories = categoriesRepository.findAllById(ids);
        
        if (foundCategories.size() != ids.size()) {
            Set<Long> foundIds = foundCategories.stream().map(Category::getId).collect(Collectors.toSet());
            String missingIds = ids.stream().filter(id -> !foundIds.contains(id))
                .map(String::valueOf).collect(Collectors.joining(", "));
            throw new ResourceNotFoundException("IDs de Categoria inválidos: " + missingIds);
        }
        return new HashSet<>(foundCategories);
    }

    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}