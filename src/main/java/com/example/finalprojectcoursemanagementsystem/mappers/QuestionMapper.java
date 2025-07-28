package com.example.finalprojectcoursemanagementsystem.mappers;

import com.example.finalprojectcoursemanagementsystem.model.dto.QuestionDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Question;
import com.example.finalprojectcoursemanagementsystem.model.request.QuestionCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionDTO mapIntoDTO(Question question);
    Question mapIntoEntity(QuestionCreateRequest request);

}
