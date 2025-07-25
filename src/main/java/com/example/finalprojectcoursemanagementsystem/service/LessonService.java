package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.model.entity.Lesson;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.CourseRepository;
import com.example.finalprojectcoursemanagementsystem.repository.LessonRepository;
import com.example.finalprojectcoursemanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private  final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public LessonDTO addLesson(Long id, Long courseId, LessonCreateRequest request) {

        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);
        if(!course.getCourseOwner().getId().equals(id)){
            throw new RuntimeException("Only the owner teacher can add lesson!");
        }
        Lesson lesson = new Lesson();
        lesson.setLessonName(request.getLessonName());
        lesson.setLessonText(request.getLessonText());
        lesson.setLessonDescription(request.getLessonDescription());
        lesson.setVideoURL(request.getVideoURL());

        course.addLesson(lesson);
        courseRepository.save(course);
        Lesson savedLesson = lessonRepository.save(lesson);

        return Lesson.mapIntoDTO(savedLesson);
    }

    @Transactional
    public LessonDTO getLesson(Long userId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(EntityNotFoundException::new);
        Course course = lesson.getCourse();
        if(userRepository.isCourseAlreadyPurchased(userId, course.getId()) || course.getCourseOwner().getId().equals(userId)){
            return Lesson.mapIntoDTO(lesson);
        } else {
            throw new RuntimeException("Only the owner teacher and enrolled users can view lesson!");
        }
    }

}
