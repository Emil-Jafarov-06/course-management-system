package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.CourseRepository;
import com.example.finalprojectcoursemanagementsystem.repository.UserRepository;
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
    public CourseDTO createCourse(Long id, CourseCreateRequest courseCreateRequest) {
        CourseUser user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found with id " + id));
        if(!user.getRole().equals(RoleEnum.TEACHER)){
            throw new RuntimeException("Only teachers can create courses!");
        }

        Course course = new Course();
        course.setCourseName(courseCreateRequest.getCourseName());
        course.setCourseDescription(courseCreateRequest.getCourseDescription());
        course.setCoursePay(courseCreateRequest.getCoursePay());
        course.setCourseOwner(user);

        user.createCourse(course);
        courseRepository.save(course);

        return Course.mapIntoDTO(course);
    }

    @Transactional
    public void enrollForCourse(Long courseUser, Long courseId) {

        CourseUser user = userRepository.findById(courseUser).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if(userRepository.findPurchasedCoursesById(courseId).contains(course)){
            throw new RuntimeException("Already enrolled for this course!");
        }

        userService.decreaseBalance(user.getId(),course.getCoursePay());
        userService.increaseBalance(course.getCourseOwner().getId(),course.getCoursePay());

        course.addLearner(user);

        userRepository.save(user);
        courseRepository.save(course);

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
