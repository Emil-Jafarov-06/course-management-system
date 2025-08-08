package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.CourseNotFoundException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.LessonNotFoundException;
import com.example.finalprojectcoursemanagementsystem.mappers.LessonMapper;
import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.entity.Lesson;
import com.example.finalprojectcoursemanagementsystem.model.entity.LessonProgress;
import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.CourseRepository;
import com.example.finalprojectcoursemanagementsystem.repository.LessonProgressRepository;
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
    private final LessonProgressRepository lessonProgressRepository;

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
        boolean isEnrolled = userRepository.isCourseAlreadyPurchased(userId, course.getId());

        if(!course.getCourseOwner().getId().equals(userId) && !isEnrolled){
            throw new ForbiddenAccessException("Only the owner teacher and enrolled users can view lesson!");
        }
        if(isEnrolled){
            LessonProgress lessonProgress = lessonProgressRepository.findLessonProgressByCourseUser_IdAndLesson_Id(userId, lessonId);
            lessonProgress.setProgress(ProgressEnum.IN_PROGRESS);
            lessonProgressRepository.save(lessonProgress);
        }
        LessonDTO lessonDTO = lessonMapper.mapIntoDTO(lesson);
        if(lesson.getQuiz() != null){
            lessonDTO.setQuizId(lesson.getQuiz().getId());
        }
        return lessonDTO;
    }

    @Transactional
    public LessonDTO updateLesson(Long id, Long lessonId, LessonUpdateRequest request) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id " + lessonId));
        if(lesson.getCourse().getCourseOwner().getId().equals(id)){
            lesson.setName(request.getName());
            lesson.setText(request.getText());
            lesson.setDescription(request.getDescription());
            lesson.setVideoURL(request.getVideoURL());
            LessonDTO lessonDTO = lessonMapper.mapIntoDTO(lessonRepository.save(lesson));
            if(lesson.getQuiz() != null){
                lessonDTO.setQuizId(lesson.getQuiz().getId());
            }
            return lessonDTO;
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
