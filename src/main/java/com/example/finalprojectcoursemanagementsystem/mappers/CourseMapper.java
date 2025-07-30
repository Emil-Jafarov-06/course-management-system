package com.example.finalprojectcoursemanagementsystem.mappers;

import com.example.finalprojectcoursemanagementsystem.model.dto.CourseDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.Course;
import com.example.finalprojectcoursemanagementsystem.model.request.CourseCreateRequest;
import org.mapstruct.Mapper;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDTO mapIntoDTO(Course course);
    Course mapIntoEntity(CourseCreateRequest request);

}
