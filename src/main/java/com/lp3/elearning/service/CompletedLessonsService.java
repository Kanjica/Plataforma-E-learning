package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.repository.CompletedLessonRepository;

@Service
public class CompletedLessonsService {

    private final CompletedLessonRepository completedLessonRepository;

    public CompletedLessonsService(CompletedLessonRepository completedLessonRepository) {
        this.completedLessonRepository = completedLessonRepository;
    }

    
    
}
