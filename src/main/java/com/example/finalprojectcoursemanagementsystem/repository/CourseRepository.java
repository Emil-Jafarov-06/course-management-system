package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findCoursesByCourseDescriptionLikeIgnoreCase(String courseDescription);

    @Query(value = "SELECT c FROM Course c WHERE LOWER(c.courseDescription) LIKE LOWER(CONCAT('%', :courseDescription, '%')) AND c.isAvailable = true",
            countQuery = "SELECT COUNT(c) FROM Course c WHERE LOWER(c.courseDescription) LIKE LOWER(CONCAT('%', :courseDescription, '%')) AND c.isAvailable = true")
    Page<Course> findCoursesByCourseDescriptionLikeIgnoreCase(@Param("courseDescription") String courseDescription, Pageable pageable);

    List<Course> findCoursesByCourseOwner_Id(Long courseOwnerId);

    @Query(value = "SELECT cu FROM CourseUser cu JOIN cu.paidCourses c WHERE c.id = :id",
            countQuery = "SELECT COUNT(cu) FROM CourseUser cu JOIN cu.paidCourses c WHERE c.id = :id")
    Page<CourseUser> findEnrolledUsersByCourseId(@Param("id") Long courseId, Pageable pageable);

    Optional<Course> findCourseByCourseNameIgnoreCase(String courseName);
}
