package com.codeit.project.slid_todo.application.auth.business;

import com.codeit.project.slid_todo.application.auth.web.dto.SignupDto;
import com.codeit.project.slid_todo.domain.user.business.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final UserService userService;

    public void registerUser(SignupDto.Request requestDto) {
        userService.registerUser(
                requestDto.name(),
                requestDto.email(),
                requestDto.password()
        );
    }

}
