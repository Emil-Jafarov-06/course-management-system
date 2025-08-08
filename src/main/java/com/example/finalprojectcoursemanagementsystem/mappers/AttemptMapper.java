package com.example.finalprojectcoursemanagementsystem.mappers;

import com.example.finalprojectcoursemanagementsystem.model.dto.AttemptDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Attempt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttemptMapper {

    AttemptDTO mapIntoDTO(Attempt attempt);

}
