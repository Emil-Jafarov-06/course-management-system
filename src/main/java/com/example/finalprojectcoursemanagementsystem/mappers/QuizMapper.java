package com.example.finalprojectcoursemanagementsystem.mappers;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuizDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Quiz;
import com.example.finalprojectcoursemanagementsystem.model.request.QuizCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuizMapper {

    QuizDTO mapIntoDTO(Quiz quiz);
    Quiz mapIntoEntity(QuizCreateRequest request);

}
