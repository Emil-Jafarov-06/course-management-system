package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO getUserById(Long id) {
        CourseUser user =  userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return CourseUser.mapIntoDTO(user);
    }

    public CourseUser getUserByUserName(String name) {
        return userRepository.findCourseUserByUserName(name).orElseThrow(EntityNotFoundException::new);
    }

    public UserDTO getUserDTOByUserName(String name) {
        CourseUser user = userRepository.findCourseUserByUserName(name).orElseThrow(EntityNotFoundException::new);
        return CourseUser.mapIntoDTO(user);
    }

    public UserDTO getUserByUserEmail(String email) {
        CourseUser user = userRepository.findCourseUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        return CourseUser.mapIntoDTO(user);
    }

    public CourseUser registerUser(CourseUser user) {
        user.setBalance(0D);
        return userRepository.save(user);
    }

}
