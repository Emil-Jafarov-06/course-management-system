package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.CourseRepository;
import com.example.finalprojectcoursemanagementsystem.repository.UserRepository;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EntityManager entityManager;

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Course not found with id " + id));
        return Course.mapIntoDTO(course);
    }

    public CourseDTO getCourseByName(String courseName) {
        Course course = courseRepository.findCourseByCourseNameIgnoreCase(courseName);
        return Course.mapIntoDTO(course);
    }

    public List<CourseDTO> searchForCourses(String courseName) {
        List<Course> courses = courseRepository.findCoursesByCourseDescriptionLikeIgnoreCase(courseName);
        return courses.stream()
                .map(Course::mapIntoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseDTO createCourse(SecurityUser securityUser, CourseCreateRequest courseCreateRequest) {
        CourseUser user = entityManager.merge(securityUser.getCourseUser());
        Course course = new Course();
        course.setCourseName(courseCreateRequest.getCourseName());
        course.setCourseDescription(courseCreateRequest.getCourseDescription());
        course.setCoursePay(courseCreateRequest.getCoursePay());
        user.createCourse(course);
        entityManager.flush();

        return Course.mapIntoDTO(course);
    }

    @Transactional
    public void enrollForCourse(SecurityUser securityUser, Long courseId) {

        CourseUser user = entityManager.merge(securityUser.getCourseUser());
        Course course = courseRepository.findById(courseId).orElseThrow();

        if(userRepository.isCourseAlreadyPurchased(user.getId(), courseId)){
            throw new RuntimeException("Already enrolled for this course!");
        }
        if(user.equals(course.getCourseOwner())){
            throw new RuntimeException("You are the owner of this course!");
        }
        if(user.getBalance() < course.getCoursePay()){
            throw new RuntimeException("Insufficient balance!");
        }

        user.setBalance(user.getBalance() - course.getCoursePay());
        CourseUser owner = course.getCourseOwner();
        owner.setBalance(owner.getBalance() + course.getCoursePay());

        course.addLearner(user);
        userRepository.save(user);

    }

    @Transactional
    public CourseDTO updateCourse(Long userId, Long courseId, CourseUpdateRequest courseUpdateRequest) {

        CourseUser user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if(!user.getRole().equals(RoleEnum.TEACHER) || course.getCourseOwner().getId().equals(user.getId())){
            throw new RuntimeException("Only the owner teacher can update courses!");
        }

        if(Objects.nonNull(courseUpdateRequest.getCourseName())){
            course.setCourseName(courseUpdateRequest.getCourseName());
        }
        if(Objects.nonNull(courseUpdateRequest.getCourseDescription())){
            course.setCourseDescription(courseUpdateRequest.getCourseDescription());
        }
        if(Objects.nonNull(courseUpdateRequest.getCoursePay())){
            course.setCoursePay(courseUpdateRequest.getCoursePay());
        }

        Course updatedCourse = courseRepository.save(course);
        return Course.mapIntoDTO(updatedCourse);

    }

    public List<CourseDTO> getCoursesFromTeacher(Long id) {

        List<Course> courses = courseRepository.findCoursesByCourseOwner_Id(id);
        return  courses.stream()
                .map(Course::mapIntoDTO)
                .collect(Collectors.toList());

    }
}
