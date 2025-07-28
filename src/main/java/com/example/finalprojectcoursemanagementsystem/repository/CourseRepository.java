package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findCoursesByCourseDescriptionLikeIgnoreCase(String courseDescription);

    List<Course> findCoursesByCourseOwner_Id(Long courseOwnerId);

    Optional<Course> findCourseByCourseNameIgnoreCase(String courseName);
}
