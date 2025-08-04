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

public interface UserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findCourseUserByUserName(String name);

    Optional<CourseUser> findCourseUserByEmail(String email);

    @Query(value = "SELECT cu FROM CourseUser cu WHERE LOWER(cu.userName) ILIKE LOWER(:userName)",
            countQuery = "SELECT COUNT(cu) FROM CourseUser cu WHERE LOWER(cu.userName) LIKE LOWER(CONCAT('%', :userName, '%'))")
    Page<CourseUser> findCourseUsersByUserNameLikeIgnoreCase(@Param("userName") String userName, Pageable pageable);

    @Query(value = "SELECT DISTINCT c FROM CourseUser cu JOIN cu.paidCourses c LEFT JOIN FETCH c.courseOwner WHERE cu.id = :id",
            countQuery = "SELECT COUNT(distinct c) FROM CourseUser cu JOIN cu.paidCourses c WHERE cu.id = :id")
    Page<Course> findPurchasedCoursesById(Long id, Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM CourseUser cu JOIN cu.paidCourses c WHERE cu.id = :userId AND c.id = :courseId")
    Boolean isCourseAlreadyPurchased(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query(value = "SELECT DISTINCT c FROM CourseUser cu JOIN cu.coursesCreated c LEFT JOIN FETCH c.courseOwner WHERE cu.id = :id",
            countQuery = "SELECT COUNT(c) FROM CourseUser cu JOIN cu.coursesCreated c WHERE cu.id = :id")
    Page<Course> findCoursesCreatedById(Long id, Pageable pageable);

}
