package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks private CategoryService categoriesService;
    @Mock private CategoryRepository categoriesRepository;

    @Test
    void shouldThrowException_WhenCategoryIdsNotFound() {
        Set<Long> ids = Set.of(1L, 99L);
        
        // Simula que nenhuma categoria foi encontrada no banco
        when(categoriesRepository.findAllById(any())).thenReturn(Collections.emptyList());

        // Verifica APENAS se a exceção correta é lançada (sem checar a mensagem de texto exata)
        assertThrows(ResourceNotFoundException.class, () -> {
            categoriesService.getCategoriesByValidIds(ids);
        });
    }
}