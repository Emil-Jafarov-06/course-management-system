package com.example.finalprojectcoursemanagementsystem.service;

import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.QuestionNotFoundException;
import com.example.finalprojectcoursemanagementsystem.mappers.QuestionMapper;
import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Question;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionUpdateRequest;
import com.example.finalprojectcoursemanagementsystem.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Transactional
    public QuestionDTO getQuestionById(Long userId, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id " + questionId));
        if(question.getQuiz().getLesson().getCourse().getCourseOwner().getId().equals(userId)){
            return questionMapper.mapIntoDTO(question);
        }
        throw new ForbiddenAccessException("Only the owner teacher and enrolled users can view question!");
    }

    public QuestionDTO updateQuestionById(Long userId, Long questionId, QuestionUpdateRequest request) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id " + questionId));
        if(!question.getQuiz().getLesson().getCourse().getCourseOwner().getId().equals(userId)){
            throw new ForbiddenAccessException("Only the owner teacher can update question!");
        }
        question.setText(request.getText());
        question.setVariantA(request.getVariantA());
        question.setVariantB(request.getVariantB());
        question.setVariantC(request.getVariantC());
        question.setVariantD(request.getVariantD());
        question.setCorrectVariant(request.getCorrectVariant());
        return questionMapper.mapIntoDTO(questionRepository.save(question));

    }

}
