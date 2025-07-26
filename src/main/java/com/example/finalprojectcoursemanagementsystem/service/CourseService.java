package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.*;
import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.*;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;

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
        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId()).orElseThrow(EntityNotFoundException::new);
        Course course = new Course();
        course.setCourseName(courseCreateRequest.getCourseName());
        course.setCourseDescription(courseCreateRequest.getCourseDescription());
        course.setCoursePay(courseCreateRequest.getCoursePay());
        user.createCourse(course);
        userRepository.save(user);

        return Course.mapIntoDTO(course);
    }

    @Transactional
    public void enrollForCourse(SecurityUser securityUser, Long courseId) {

        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId()).orElseThrow(EntityNotFoundException::new);
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

        CourseProgress courseProgress = CourseProgress.builder()
                .courseUser(user)
                .course(course)
                .progress(ProgressEnum.NOT_STARTED)
                .completedUnits(0)
                .totalUnits(course.getLessons().size()).build();
        user.getCourseProgressList().add(courseProgress);


        List<LessonProgress> lessonProgressList = new ArrayList<>();
        for(Lesson lesson : course.getLessons()) {
            LessonProgress lessonProgress = LessonProgress.builder()
                    .courseUser(user)
                    .lesson(lesson)
                    .progress(ProgressEnum.NOT_STARTED).build();
            user.getLessonProgressList().add(lessonProgress);
            lessonProgressList.add(lessonProgress);
        }


        lessonProgressRepository.saveAll(lessonProgressList);
        courseProgressRepository.save(courseProgress);
        userRepository.saveAll(List.of(user, owner));

    }

    @Transactional
    public CourseDTO updateCourse(Long userId, Long courseId, CourseUpdateRequest courseUpdateRequest) {

        CourseUser user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if(!course.getCourseOwner().getId().equals(user.getId())){
            throw new RuntimeException("Only the owner teacher can update courses!");
        }

        course.setCourseName(courseUpdateRequest.getCourseName());
        course.setCourseDescription(courseUpdateRequest.getCourseDescription());
        course.setCoursePay(courseUpdateRequest.getCoursePay());

        Course updatedCourse = courseRepository.save(course);
        return Course.mapIntoDTO(updatedCourse);

    }

    public List<CourseDTO> getCoursesFromTeacher(Long id) {

        List<Course> courses = courseRepository.findCoursesByCourseOwner_Id(id);
        return  courses.stream()
                .map(Course::mapIntoDTO)
                .collect(Collectors.toList());

    }

    @Transactional
    public LessonDTO continueCourse(Long userId, Long courseId) {

        CourseUser user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if(!userRepository.isCourseAlreadyPurchased(user.getId(), courseId)){
            throw new RuntimeException("Only the enrolled users can continue courses!");
        }

        for(Lesson lesson : course.getLessons()) {
            LessonProgress progress = lessonProgressRepository.findLessonProgressByCourseUser_IdAndLesson_Id(userId, lesson.getId());
            if(progress.getProgress().equals(ProgressEnum.COMPLETED)) {
                continue;
            } else {
                progress.setProgress(ProgressEnum.IN_PROGRESS);
                lessonProgressRepository.save(progress);
                lessonRepository.save(lesson);
                return Lesson.mapIntoDTO(lesson);
            }
        }
        throw new RuntimeException("All lessons have been completed!");

    }
}
