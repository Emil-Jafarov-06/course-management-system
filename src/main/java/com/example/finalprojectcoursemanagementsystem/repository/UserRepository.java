package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findCourseUserByUserName(String name);

    Optional<CourseUser> findCourseUserByEmail(String email);
}
