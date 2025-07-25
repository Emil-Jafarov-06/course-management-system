package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Question;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.QuestionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionDTO getQuestionById(Long userId, Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(EntityExistsException::new);
        if(question.getQuiz().getLesson().getCourse().getCourseOwner().getId().equals(userId)){
            return Question.mapIntoDTO(question);
        }
        throw new RuntimeException("Only the owner teacher and enrolled users can view question!");
    }


    public QuestionDTO updateQuestionById(Long userId, Long questionId, QuestionUpdateRequest request) {

        Question question = questionRepository.findById(questionId).orElseThrow(EntityExistsException::new);
        if(!question.getQuiz().getLesson().getCourse().getCourseOwner().getId().equals(userId)){
            throw new RuntimeException("Only the owner teacher can update question!");
        }
        question.setQuestionText(request.getQuestionText());
        question.setVariantA(request.getVariantA());
        question.setVariantB(request.getVariantB());
        question.setVariantC(request.getVariantC());
        question.setVariantD(request.getVariantD());
        question.setCorrectVariant(request.getCorrectVariant());
        return Question.mapIntoDTO(questionRepository.save(question));

    }

}
