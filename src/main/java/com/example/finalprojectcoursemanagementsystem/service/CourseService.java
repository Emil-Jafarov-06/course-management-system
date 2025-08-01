package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.AlreadyEnrolledException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.InsufficientBalanceException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.NonAvailableCourseException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.CourseNotFoundException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.UserNotFoundException;
import com.example.finalprojectcoursemanagementsystem.mappers.CourseMapper;
import com.example.finalprojectcoursemanagementsystem.mappers.LessonMapper;
import com.example.finalprojectcoursemanagementsystem.mappers.UserMapper;
import com.example.finalprojectcoursemanagementsystem.model.PageImplementation;
import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.*;
import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.model.response.LessonResponseForInfo;
import com.example.finalprojectcoursemanagementsystem.repository.*;
import com.example.finalprojectcoursemanagementsystem.security.SecurityUser;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new CourseNotFoundException("Course not found with id " + id));
        return courseMapper.mapIntoDTO(course);
    }

    public CourseDTO getCourseByName(String courseName) {
        Course course = courseRepository.findCourseByCourseNameIgnoreCase(courseName)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with name " + courseName));
        return courseMapper.mapIntoDTO(course);
    }

    public PageImplementation<CourseDTO> searchForCourses(@NotBlank String name, @PositiveOrZero int page) {
        Page<Course> pagedCourses = courseRepository.findCoursesByCourseDescriptionLikeIgnoreCase(name, PageRequest.of(page, 10));
        Page<CourseDTO> pagedCoursesDto = pagedCourses.map(courseMapper::mapIntoDTO);
        return new PageImplementation<>(pagedCoursesDto);
    }

    @Transactional
    public CourseDTO createCourse(SecurityUser securityUser, CourseCreateRequest courseCreateRequest) {
        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + securityUser.getCourseUser().getId()));
        Course course = courseMapper.mapIntoEntity(courseCreateRequest);
        course.setAvailable(false);
        user.createCourse(course);
        course.setCourseOwner(user);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.mapIntoDTO(savedCourse);
    }

    @Transactional
    public void enrollForCourse(SecurityUser securityUser, Long courseId) {

        CourseUser user = userRepository.findById(securityUser.getCourseUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + securityUser.getCourseUser().getId()));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseId));

        if(!course.isAvailable()){
            throw new NonAvailableCourseException("This course is not available!");
        }
        if(userRepository.isCourseAlreadyPurchased(user.getId(), courseId)){
            throw new AlreadyEnrolledException("Already enrolled in this course!");
        }
        if(user.equals(course.getCourseOwner())){
            throw new RuntimeException("You are the owner of this course!");
        }
        if(user.getBalance() < course.getCoursePay()){
            throw new InsufficientBalanceException("Insufficient balance!");
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
                    .quizStarted(null)
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

        CourseUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseId));

        if(!course.getCourseOwner().getId().equals(user.getId())){
            throw new ForbiddenAccessException("You are not allowed to update this course!");
        }

        course.setCourseName(courseUpdateRequest.getCourseName());
        course.setCourseDescription(courseUpdateRequest.getCourseDescription());
        course.setCoursePay(courseUpdateRequest.getCoursePay());

        Course updatedCourse = courseRepository.save(course);
        return courseMapper.mapIntoDTO(updatedCourse);

    }

    public PageImplementation<CourseDTO> getCoursesFromTeacher(Long id, int page) {
        Page<Course> courses = userRepository.findCoursesCreatedById(id, PageRequest.of(page, 10));
        Page<CourseDTO> pagedCourses = courses.map(courseMapper::mapIntoDTO);
        return new PageImplementation<>(pagedCourses);
    }

    @Transactional
    public LessonDTO continueCourse(Long userId, Long courseId) {

        CourseUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseId));

        if(!userRepository.isCourseAlreadyPurchased(user.getId(), courseId)){
            throw new ForbiddenAccessException("Only the enrolled users can continue courses!");
        }

        for(Lesson lesson : course.getLessons()) {
            LessonProgress progress = lessonProgressRepository.findLessonProgressByCourseUser_IdAndLesson_Id(userId, lesson.getId());
            if(progress.getProgress().equals(ProgressEnum.COMPLETED)) {
                continue;
            } else {
                progress.setProgress(ProgressEnum.IN_PROGRESS);
                lessonProgressRepository.save(progress);
                lessonRepository.save(lesson);
                LessonDTO lessonDTO = lessonMapper.mapIntoDTO(lesson);
                lessonDTO.setQuizId(lesson.getQuiz().getId());
                return lessonDTO;
            }
        }
        throw new RuntimeException("All lessons have been completed!");
    }

    @Transactional
    public List<LessonResponseForInfo> getLessonsInfoForCourse(Long userId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseId));
        if(!userRepository.isCourseAlreadyPurchased(userId, course.getId()) && !course.getCourseOwner().getId().equals(userId)){
            throw new ForbiddenAccessException("Only the owner teacher and enrolled users can view lessons!");
        }
        List<Lesson> lessons = lessonRepository.findLessonsByCourse_Id(courseId);
        return lessons.stream()
                .map(lesson -> {
                    return new LessonResponseForInfo(lesson.getId(), lesson.getLessonName(), lesson.getLessonDescription());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public PageImplementation<UserDTO> getEnrolledUsers(Long userId, Long courseId, @PositiveOrZero int page) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseId));
        if(!course.getCourseOwner().getId().equals(userId)){
            throw new ForbiddenAccessException("Only the owner teacher can view enrolled users!");
        }
        Page<CourseUser> pagedUsers = courseRepository.findEnrolledUsersByCourseId(courseId, PageRequest.of(page, 10));
        Page<UserDTO> pagedUsersDto=  pagedUsers.map(userMapper::toUserDTO);
        return new PageImplementation<>(pagedUsersDto);
    }

    @Transactional
    public CourseDTO setCourseAvailability(Long userId, Long courseId, boolean available) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseId));
        if(!course.getCourseOwner().getId().equals(userId)){
            throw new ForbiddenAccessException("Only the owner teacher can modify the course availability status!");
        }
        course.setAvailable(available);
        return courseMapper.mapIntoDTO(courseRepository.save(course));
    }
}
