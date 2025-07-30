package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.IncorrectUsernamePasswordException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.InsufficientBalanceException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.UserNotFoundException;
import com.example.finalprojectcoursemanagementsystem.mappers.CourseMapper;
import com.example.finalprojectcoursemanagementsystem.mappers.UserMapper;
import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.AccountDeleteRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.UsernamePasswordUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.UserRepository;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;

    public UserDTO getUserById(Long id) {
        CourseUser user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id " + id + "!"));
        return userMapper.toUserDTO(user);
    }

    public CourseUser getUserByUserName(String name) {
        return userRepository.findCourseUserByUserName(name)
                .orElseThrow(()-> new UserNotFoundException("User not found with name " + name + "!"));
    }

    public UserDTO getUserDTOByUserName(String name) {
        CourseUser user = userRepository.findCourseUserByUserName(name)
                .orElseThrow(() -> new UserNotFoundException("User not found with name " + name + "!"));
        return userMapper.toUserDTO(user);
    }

    public UserDTO getUserByUserEmail(String email) {
        CourseUser user = userRepository.findCourseUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email + "!"));
        return userMapper.toUserDTO(user);
    }

    public CourseUser registerUser(CourseUser user) {
        user.setBalance(0D);
        return userRepository.save(user);
    }

    /*
    public List<CourseDTO> getPurchasedCourses(Long userId) {
        List<Course> courses = userRepository.findPurchasedCoursesById(userId);
        return courses.stream()
                .map(courseMapper::mapIntoDTO)
                .collect(Collectors.toList());
    }
     */

    public Page<CourseDTO> getPurchasedCourses(Long userId, int page){
        Page<Course> pagedCourses = userRepository.findPurchasedCoursesById(userId, PageRequest.of(page, 10));
        return pagedCourses.map(courseMapper::mapIntoDTO);
    }

    /*
    public List<CourseDTO> getCreatedCourses(Long userId) {
        List<Course> courses = userRepository.findCoursesCreatedById(userId);
        return courses.stream()
                .map(courseMapper::mapIntoDTO)
                .collect(Collectors.toList());
    }

     */

    public Page<CourseDTO> getCreatedCourses(Long userId, int page){
        Page<Course> pagedCourses = userRepository.findCoursesCreatedById(userId, PageRequest.of(page, 10));
        return pagedCourses.map(courseMapper::mapIntoDTO);
    }

    @Transactional
    public Double increaseBalance(SecurityUser securityUser, Double amount) {
        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId())
                .orElseThrow(()-> new UserNotFoundException("User not found with id " + securityUser.getCourseUser().getId() + "!"));
        Double updatedAmount = user.getBalance() + amount;
        user.setBalance(updatedAmount);
        userRepository.save(user);
        return updatedAmount;
    }

    @Transactional
    public Double decreaseBalance(SecurityUser securityUser, Double amount) {
        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + securityUser.getCourseUser().getId() + "!"));
        if(user.getBalance() >= amount) {
            Double updatedAmount = user.getBalance() - amount;
            user.setBalance(updatedAmount);
            userRepository.save(user);
            return updatedAmount;
        }
        throw new InsufficientBalanceException("Insufficient balance!");
    }

    @Transactional
    public UserDTO changeEmail(SecurityUser securityUser, String newEmail) {
        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId()).orElseThrow(EntityNotFoundException::new);
        if(user.getEmail().equals(newEmail)){
            throw new RuntimeException("Different email address must be provided!");
        }
        user.setEmail(newEmail);
        CourseUser updatedUser = userRepository.save(user);
        return userMapper.toUserDTO(updatedUser);
    }

    @Transactional
    public UserDTO updateUsernamePassword(SecurityUser securityUser, UsernamePasswordUpdateRequest request) {
        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + securityUser.getCourseUser().getId() + "!"));
        if(!user.getUserName().equals(request.getOldUsername())){
            throw new ForbiddenAccessException("Cannot update another account!");
        }
        if(passwordEncoder.matches(request.getOldPassword(), user.getEncryptedPassword())) {
            user.setUserName(request.getNewUsername());
            user.setEncryptedPassword(passwordEncoder.encode(request.getNewPassword()));
            CourseUser updatedUser = userRepository.save(user);
            return userMapper.toUserDTO(updatedUser);
        } else{
            throw new IncorrectUsernamePasswordException("Username or password is not correct!");
        }
    }

    @Transactional
    public void deleteAccount(SecurityUser securityUser, AccountDeleteRequest request) {
        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + securityUser.getCourseUser().getId() + "!"));
        if(!user.getUserName().equals(request.getUsername())){
            throw new ForbiddenAccessException("Cannot delete another account!");
        }
        if(passwordEncoder.matches(request.getPassword(), securityUser.getPassword())) {
            userRepository.deleteById(user.getId());
        } else{
            throw new IncorrectUsernamePasswordException("Username or password is not correct!");
        }
    }

    public List<UserDTO> getAllUsers() {
        List<CourseUser> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    /*
    public List<UserDTO> getUsersByNameLike(String name) {

        List<CourseUser> users = userRepository.findCourseUsersByUserNameLikeIgnoreCase("%" + name + "%");
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }
     */

    public Page<UserDTO> getUsersByNameLike(@NotBlank String name, @PositiveOrZero int page) {
        Page<CourseUser> pagedUsers = userRepository.findCourseUsersByUserNameLikeIgnoreCase("%" + name + "%" , PageRequest.of(page, 10));
        return pagedUsers.map(userMapper::toUserDTO);
    }

    public String addNewAdmin(Long userId) {
        CourseUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id " + userId + "!"));
        user.setRole(RoleEnum.ADMIN);
        userRepository.save(user);
        return "User with id " + userId + " has been set admin!";
    }

    public String takeAdminRoleFromUser(Long userId) {
        CourseUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id " + userId + "!"));
        user.setRole(RoleEnum.LEARNER);
        userRepository.save(user);
        return "Admin with id " + userId + " has been removed!";
    }
}
