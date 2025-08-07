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

    List<Course> findCoursesByDescriptionLikeIgnoreCase(String courseDescription);

    @Query(value = """
    SELECT c FROM Course c\s
    LEFT JOIN c.ratings r\s
    WHERE LOWER(c.description) LIKE LOWER(CONCAT('%', :courseDescription, '%'))\s
    AND c.available = true\s
    GROUP BY c.id\s
    ORDER BY AVG(r.rating) DESC
   \s""",
            countQuery = """
    SELECT COUNT(c) FROM Course c 
    WHERE LOWER(c.description) LIKE LOWER(CONCAT('%', :courseDescription, '%')) 
    AND c.available = true
    """)
    Page<Course> findCoursesByDescriptionLikeIgnoreCase(@Param("courseDescription") String courseDescription, Pageable pageable);

    List<Course> findCoursesByCourseOwner_Id(Long courseOwnerId);

    @Query(value = "SELECT cu FROM CourseUser cu JOIN cu.paidCourses c WHERE c.id = :id",
            countQuery = "SELECT COUNT(cu) FROM CourseUser cu JOIN cu.paidCourses c WHERE c.id = :id")
    Page<CourseUser> findEnrolledUsersByCourseId(@Param("id") Long courseId, Pageable pageable);

    Optional<Course> findCourseByNameIgnoreCase(String name);
}
