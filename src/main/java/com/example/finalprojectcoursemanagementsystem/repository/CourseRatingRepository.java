package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {
    Optional<CourseRating> findByUser_IdAndCourse_Id(Long userId, Long courseId);

    long countCourseRatingsByCourse_Id(Long courseId);

    @Query("SELECT AVG(cr.rating) FROM CourseRating cr WHERE cr.course.id = :courseId")
    Double calculateAverageRating(@Param("courseId") Long courseId);

}
