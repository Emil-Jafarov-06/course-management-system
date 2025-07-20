package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findCourseUserByUserName(String name);

    Optional<CourseUser> findCourseUserByEmail(String email);

    @Query("SELECT c FROM CourseUser cu JOIN cu.paidCourses c where cu.id = :id")
    List<Course> findPurchasedCoursesById(Long id);

}
