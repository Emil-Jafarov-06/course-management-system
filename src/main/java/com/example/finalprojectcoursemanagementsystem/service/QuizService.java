package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.LessonNotFoundException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.QuizNotFoundException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.UserNotFoundException;
import com.example.finalprojectcoursemanagementsystem.mappers.QuestionMapper;
import com.example.finalprojectcoursemanagementsystem.mappers.QuizMapper;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.*;
import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizSubmitRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final EmailService emailService;
    private final QuestionMapper questionMapper;
    private final QuizMapper quizMapper;

    @Transactional
    public QuizDTO getQuiz(Long userId, Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id " + quizId));
        Course course = quiz.getLesson().getCourse();

        if(userRepository.isCourseAlreadyPurchased(userId, course.getId())) {
            LessonProgress lessonProgress = lessonProgressRepository.findLessonProgressByCourseUser_IdAndLesson_Id(userId, quiz.getLesson().getId());
            lessonProgress.setQuizStarted(LocalDateTime.now());
            lessonProgressRepository.save(lessonProgress);

            return quizMapper.mapIntoDTO(quiz);
        }
        if(course.getCourseOwner().getId().equals(userId)) {
            return quizMapper.mapIntoDTO(quiz);
        }
        throw new ForbiddenAccessException("You are not allowed to view this quiz!");
    }

    @Transactional
    public String checkQuiz(Long userId, Long quizId, QuizSubmitRequest request) {
        CourseUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId + "!"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id " + quizId));
        Course course = quiz.getLesson().getCourse();

        if(!userRepository.isCourseAlreadyPurchased(userId, course.getId())) {
            throw new ForbiddenAccessException("You are not allowed to submit this quiz!");
        }

        LessonProgress progress = lessonProgressRepository.findLessonProgressByCourseUser_IdAndLesson_Id(userId, quiz.getLesson().getId());
        if(progress.getQuizStarted() == null){
            throw new ForbiddenAccessException("Cannot submit quiz before starting it! Please try again later!");
        }
        LocalDateTime quizFinishTime = progress.getQuizStarted().plusMinutes(quiz.getDuration());
        if(quizFinishTime.isBefore(LocalDateTime.now())){
            throw new ForbiddenAccessException("Cannot submit quiz after the quiz duration has expired! Please try again later!");
        }
        int accurateResponses = 0;
        Map<Long, String> userResponses = request.getAnswers();
        for(Question question : quiz.getQuestions()) {
            String userAnswer = userResponses.get(question.getId());
            if(userAnswer != null && userAnswer.equals(question.getCorrectVariant())) {
                accurateResponses++;
            }
        }

        double correctness = (double) accurateResponses / quiz.getQuestions().size();
        if(correctness >= 0.75){
            Lesson lesson = quiz.getLesson();
            LessonProgress lessonProgress = lessonProgressRepository.findLessonProgressByCourseUser_IdAndLesson_Id(userId, lesson.getId());
            lessonProgress.setProgress(ProgressEnum.COMPLETED);
            lessonProgressRepository.save(lessonProgress);
            CourseProgress courseProgress = courseProgressRepository.findCourseProgressByCourse_IdAndCourseUser_Id(course.getId(), userId);
            courseProgress.setCompletedUnits(courseProgress.getCompletedUnits() + 1);
            if(courseProgress.getCompletedUnits() == courseProgress.getTotalUnits()){
                courseProgress.setProgress(ProgressEnum.COMPLETED);
                courseProgressRepository.save(courseProgress);
                emailService.sendSimpleEmail(user.getEmail(),
                        "Course Completion",
                        "You have successfully completed the course : " + course.getCourseName() + " by " + course.getCourseOwner().getUserName());
                return String.format("Your score is %d out of %d. You successfully completed the lesson and the course!\n Please check your email.", accurateResponses, quiz.getQuestions().size());
            } else{
                courseProgress.setProgress(ProgressEnum.IN_PROGRESS);
                courseProgressRepository.save(courseProgress);
                return String.format("Your score is %d out of %d. You successfully completed the lesson!", accurateResponses, quiz.getQuestions().size());
            }
        } else{
            return String.format("Your score is %d out of %d. You failed the lesson. Retake the quiz.", accurateResponses, quiz.getQuestions().size());
        }

    }

    @Transactional
    public QuizDTO createQuiz(Long userId, QuizCreateRequest request) {

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found with id " + request.getLessonId()));
        Course course = lesson.getCourse();
        if(!course.getCourseOwner().getId().equals(userId)) {
            throw new ForbiddenAccessException("Only owner teachers can create quizzes!");
        }
        Quiz quiz = Quiz.builder()
                .duration(request.getDuration())
                .quizDescription(request.getQuizDescription())
                .lesson(lesson).build();
        lesson.setQuiz(quiz);
        Quiz savedQuiz = quizRepository.save(quiz);
        return quizMapper.mapIntoDTO(savedQuiz);
    }

    @Transactional
    public QuestionDTO addQuestionToQuiz(Long userId, Long quizId, QuestionCreateRequest request) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id " + quizId));
        Course course = quiz.getLesson().getCourse();
        if(!course.getCourseOwner().getId().equals(userId)) {
            throw new ForbiddenAccessException("Only owner teachers modify quizzes!");
        }
        Question question = questionMapper.mapIntoEntity(request);
        quiz.getQuestions().add(question);
        question.setQuiz(quiz);
        quizRepository.save(quiz);
        return questionMapper.mapIntoDTO(questionRepository.save(question));

    }

    @Transactional
    public String deleteQuestionFromQuiz(Long id, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuizNotFoundException("Question not found with id " + questionId));
        Quiz quiz = question.getQuiz();
        if(!quiz.getLesson().getCourse().getCourseOwner().getId().equals(id)) {
            throw new ForbiddenAccessException("Only owner teachers modify quizzes!");
        }
        quiz.getQuestions().remove(question);
        questionRepository.delete(question);
        return String.format("Question with id %d deleted successfully!", questionId);
    }

    @Transactional
    public QuizDTO updateQuiz(Long id, Long quizId, QuizUpdateRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id " + quizId));
        Course course = quiz.getLesson().getCourse();
        if(!course.getCourseOwner().getId().equals(id)) {
            throw new ForbiddenAccessException("Only owner teachers modify quizzes!");
        }
        quiz.setDuration(request.getDuration());
        quiz.setQuizDescription(request.getQuizDescription());
        Quiz savedQuiz = quizRepository.save(quiz);
        return quizMapper.mapIntoDTO(savedQuiz);
    }

    public String deleteQuiz(Long id, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id " + quizId));
        Lesson lesson = quiz.getLesson();
        Course course = lesson.getCourse();
        if(!course.getCourseOwner().getId().equals(id)) {
            throw new ForbiddenAccessException("Only owner teachers modify quizzes!");
        }
        lesson.setQuiz(null);
        lessonRepository.save(lesson);
        quizRepository.delete(quiz);
        return String.format("Quiz with id %d deleted successfully!", quizId);
    }
}
