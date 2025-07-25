package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findCourseUserByUserName(String name);

    Optional<CourseUser> findCourseUserByEmail(String email);

    @Query("SELECT DISTINCT c FROM CourseUser cu JOIN cu.paidCourses c LEFT JOIN FETCH c.courseOwner WHERE cu.id = :id")
    List<Course> findPurchasedCoursesById(Long id);

    @Query("SELECT COUNT(c) > 0 FROM CourseUser cu JOIN cu.paidCourses c WHERE cu.id = :userId AND c.id = :courseId")
    Boolean isCourseAlreadyPurchased(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT DISTINCT c FROM CourseUser cu JOIN cu.coursesCreated c LEFT JOIN FETCH c.courseOwner WHERE cu.id = :id")
    List<Course> findCoursesCreatedById(Long id);

}
