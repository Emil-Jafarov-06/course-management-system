package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
    List<Lesson> findLessonsByCourse_Id(Long courseId);
}
