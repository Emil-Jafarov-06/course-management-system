package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.model.request.UserCreateRequest;
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

    public UserDTO getUserByUserName(String name) {
        CourseUser user = userRepository.findCourseUserByUserName(name).orElseThrow(EntityNotFoundException::new);
        return CourseUser.mapIntoDTO(user);
    }

    public UserDTO getUserByUserEmail(String email) {
        CourseUser user = userRepository.findCourseUserByEmail(email).orElseThrow(EntityNotFoundException::new);
        return CourseUser.mapIntoDTO(user);
    }

    public UserDTO registerUser(UserCreateRequest user) {
        CourseUser courseUser = new CourseUser();
        courseUser.setUserName(user.getUserName());
        courseUser.setEmail(user.getEmail());
        courseUser.setEncryptedPassword(user.getPassword());
        userRepository.save(courseUser);
        return CourseUser.mapIntoDTO(courseUser);
    }
}
