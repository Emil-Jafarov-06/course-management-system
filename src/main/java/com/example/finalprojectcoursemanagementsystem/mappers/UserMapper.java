package com.example.finalprojectcoursemanagementsystem.mappers;

import com.example.finalprojectcoursemanagementsystem.model.dto.UserDTO;
import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper{
    UserDTO toUserDTO(CourseUser courseUser);
}
