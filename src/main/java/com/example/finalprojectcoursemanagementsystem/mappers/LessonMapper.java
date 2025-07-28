package com.example.finalprojectcoursemanagementsystem.mappers;

import com.example.finalprojectcoursemanagementsystem.model.dto.LessonDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Lesson;
import com.example.finalprojectcoursemanagementsystem.model.request.LessonCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonDTO mapIntoDTO(Lesson lesson);
    Lesson mapIntoEntity(LessonCreateRequest request);

}
