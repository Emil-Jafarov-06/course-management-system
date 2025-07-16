package com.example.finalprojectcoursemanagementsystem.security;

import com.example.finalprojectcoursemanagementsystem.model.entity.CourseUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final CourseUser courseUser;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + courseUser.getRole()));
    }

    @Override
    public String getPassword() {
        return courseUser.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return courseUser.getUserName();
    }
}
