package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseProgressRepository extends JpaRepository<CourseProgress,Long> {

    CourseProgress findCourseProgressByCourse_IdAndCourseUser_Id(Long courseId, Long courseUserId);

}
