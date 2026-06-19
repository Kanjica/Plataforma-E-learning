package com.lp3.elearning.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import com.lp3.elearning.security.anottation.Auditable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "categories")
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @CacheEvict(cacheNames = "courses", allEntries = true)
    @Auditable(action = "CRIAR_CATEGORIA")
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        category = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(category);
    }
    
    @Transactional
    @Auditable(action = "ATUALIZAR_CATEGORIA")
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = findCategoryEntityById(id);
        category.setName(dto.name());

        CategoryResponseDTO response = categoryMapper.toResponseDTO(categoryRepository.save(category));
        this.executeEvictCategory(id);
        return response;
    }

    @Transactional
    @Auditable(action = "DELETAR_CATEGORIA")
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada.");
        }
        categoryRepository.deleteById(id);
        this.executeEvictCategory(id);
    }

    @Cacheable(key = "'paged-'#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CategoryResponseDTO> findAllPaged(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toResponseDTO);
    }

    @Cacheable(key = "#id")
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

    @Caching(evict = {
        @CacheEvict(key = "#id"),
        @CacheEvict(allEntries = true),
        @CacheEvict(cacheNames = "courses", allEntries = true)
    })
    public void executeEvictCategory(Long id) {}
}