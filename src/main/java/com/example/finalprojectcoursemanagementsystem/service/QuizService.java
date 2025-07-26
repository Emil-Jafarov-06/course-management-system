package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.*;
import com.example.finalprojectcoursemanagementsystem.model.enums.ProgressEnum;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizCreateRequest;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizSubmitRequest;
import com.example.finalprojectcoursemanagementsystem.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final UserService userService;

    @Transactional
    public QuizDTO getQuiz(Long userId, Long quizId) {

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
        Course course = quiz.getLesson().getCourse();

        if(!userRepository.isCourseAlreadyPurchased(userId, course.getId())) {
            throw new RuntimeException("Only enrolled users can take quizzes!");
        }

        QuizDTO quizDTO = Quiz.mapIntoDTO(quiz);
        List<Question> questions = questionRepository.getQuestionsByQuiz_Id(quizId);
        quizDTO.setQuestions(questions.stream()
                .map(Question::mapIntoDTO)
                .collect(Collectors.toList()));

        return quizDTO;
    }

    @Transactional
    public String checkQuiz(Long userId, Long quizId, QuizSubmitRequest request) {

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
        Course course = quiz.getLesson().getCourse();

        if(!course.getCourseOwner().getId().equals(userId)) {
            throw new RuntimeException("Only enrolled users can submit quizzes!");
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
                userService.sendEmailAboutComplation();
                return String.format("Your score is %d out of %d. You successfully completed the lesson and the course!", accurateResponses, quiz.getQuestions().size());
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
                .orElseThrow(EntityNotFoundException::new);
        Course course = lesson.getCourse();
        if(!course.getCourseOwner().getId().equals(userId)) {
            throw new RuntimeException("Only owner teachers can create quizzes!");
        }
        Quiz quiz = Quiz.builder()
                .duration(request.getDuration())
                .quizDescription(request.getQuizDescription())
                .lesson(lesson).build();
        lesson.setQuiz(quiz);
        Quiz savedQuiz = quizRepository.save(quiz);
        return Quiz.mapIntoDTO(savedQuiz);
    }

    @Transactional
    public QuestionDTO addQuestionToQuiz(Long userId, Long quizId, QuestionCreateRequest request) {

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(EntityNotFoundException::new);
        Course course = quiz.getLesson().getCourse();
        if(!course.getCourseOwner().getId().equals(userId)) {
            throw new RuntimeException("Only owner teachers modify quizzes!");
        }
        Question question = Question.builder()
                .questionText(request.getQuestionText())
                .quiz(quiz)
                .correctVariant(request.getCorrectVariant())
                .variantA(request.getVariantA())
                .variantB(request.getVariantB())
                .variantC(request.getVariantC())
                .variantD(request.getVariantD()).build();

        quiz.getQuestions().add(question);
        question.setQuiz(quiz);
        quizRepository.save(quiz);
        return Question.mapIntoDTO(questionRepository.save(question));

    }

    @Transactional
    public String deleteQuestionFromQuiz(Long id, Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);
        Quiz quiz = question.getQuiz();
        if(!quiz.getLesson().getCourse().getCourseOwner().getId().equals(id)) {
            throw new RuntimeException("Only owner teachers modify quizzes!");
        }
        quiz.getQuestions().remove(question);
        questionRepository.delete(question);
        return String.format("Question with id %d deleted successfully!", questionId);
    }
}
