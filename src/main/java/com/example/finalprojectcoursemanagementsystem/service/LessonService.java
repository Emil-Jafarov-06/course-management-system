package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.CourseNotFoundException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.LessonNotFoundException;
import com.example.finalprojectcoursemanagementsystem.mappers.LessonMapper;
import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.Lesson;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.CourseRepository;
import com.example.finalprojectcoursemanagementsystem.repository.LessonRepository;
import com.example.finalprojectcoursemanagementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private  final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

    @Transactional
    public LessonDTO addLesson(Long id, Long courseId, LessonCreateRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseId));
        if(!course.getCourseOwner().getId().equals(id)){
            throw new ForbiddenAccessException("Only the owner teacher can add lesson!");
        }
        Lesson lesson = lessonMapper.mapIntoEntity(request);

        course.addLesson(lesson);
        Lesson savedLesson = lessonRepository.save(lesson);

        return lessonMapper.mapIntoDTO(savedLesson);
    }

    @Transactional
    public LessonDTO getLesson(Long userId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id " + lessonId));
        Course course = lesson.getCourse();
        if(userRepository.isCourseAlreadyPurchased(userId, course.getId()) || course.getCourseOwner().getId().equals(userId)){
            return lessonMapper.mapIntoDTO(lesson);
        } else {
            throw new ForbiddenAccessException("Only the owner teacher and enrolled users can view lesson!");
        }
    }

    @Transactional
    public LessonDTO updateLesson(Long id, Long lessonId, LessonUpdateRequest request) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id " + lessonId));
        if(lesson.getCourse().getCourseOwner().getId().equals(id)){
            lesson.setLessonName(request.getLessonName());
            lesson.setLessonText(request.getLessonText());
            lesson.setLessonDescription(request.getLessonDescription());
            lesson.setVideoURL(request.getVideoURL());
            return lessonMapper.mapIntoDTO(lessonRepository.save(lesson));
        }
        throw new ForbiddenAccessException("Only the owner teacher can update lesson!");

    }

    @Transactional
    public String deleteLesson(Long userId, Long lessonId) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id " + lessonId));
        Course course = lesson.getCourse();
        if(course.getCourseOwner().getId().equals(userId)){
            lessonRepository.deleteById(lessonId);
            return "Lesson deleted successfully!";
        } else {
            throw new ForbiddenAccessException("Only the owner teacher and enrolled users can delete lesson!");
        }

    }
}
