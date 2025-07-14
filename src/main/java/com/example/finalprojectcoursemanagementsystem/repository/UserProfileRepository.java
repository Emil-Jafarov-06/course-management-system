package com.example.finalprojectcoursemanagementsystem.repository;

import com.example.finalprojectcoursemanagementsystem.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
}
