package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CourseUser, Long> {



}
