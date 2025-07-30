package com.example.finalprojectcoursemanagementsystem.config;

import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import com.example.finalprojectcoursemanagementsystem.model.enums.RoleEnum;
import com.example.finalprojectcoursemanagementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class AdminSetter implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {

        if (userRepository.findCourseUserByUserName("admin").isEmpty()) {

            CourseUser admin = CourseUser.builder()
                    .role(RoleEnum.HEAD_ADMIN)
                    .userName("admin")
                    .email("exampleAdmin@gmail.com")
                    .encryptedPassword(passwordEncoder.encode("admin123321nimda"))
                    .build();

            userRepository.save(admin);
            System.out.println("️Admin user created: admin / admin123321nimda");
        }

    }



}

