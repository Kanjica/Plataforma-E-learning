package com.lp3.elearning.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.course.CategoryRequestDTO;
import com.lp3.elearning.dto.course.CategoryResponseDTO;
import com.lp3.elearning.entities.Category;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.mapper.CategoryMapper;
import com.lp3.elearning.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        category = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(category);
    }
    
    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = findCategoryEntityById(id);
        category.setName(dto.name());

        category = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(category);
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada.");
        }
        categoryRepository.deleteById(id);
    }

    public Page<CategoryResponseDTO> findAllPaged(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toResponseDTO);
    }

    public CategoryResponseDTO findById(Long id) {
        return categoryMapper.toResponseDTO(findCategoryEntityById(id));
    }

    public Category findCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    /**
     * Valida uma lista de IDs e retorna as entidades correspondentes.
     * @throws ResourceNotFoundException se algum ID não existir.
     */
    public Set<Category> getCategoriesByValidIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        
        List<Category> foundCategories = categoryRepository.findAllById(ids);
        
        if (foundCategories.size() != ids.size()) {
            Set<Long> foundIds = foundCategories.stream().map(Category::getId).collect(Collectors.toSet());
            String missingIds = ids.stream().filter(id -> !foundIds.contains(id))
                .map(String::valueOf).collect(Collectors.joining(", "));
            throw new ResourceNotFoundException("IDs de Categoria inválidos: " + missingIds);
        }
        return new HashSet<>(foundCategories);
    }
}