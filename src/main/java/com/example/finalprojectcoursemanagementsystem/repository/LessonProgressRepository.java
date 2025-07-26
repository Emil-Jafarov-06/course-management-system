package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonProgressRepository extends JpaRepository<LessonProgress,Long> {

    LessonProgress findLessonProgressByCourseUser_IdAndLesson_Id(Long courseUserId, Long lessonId);

}
