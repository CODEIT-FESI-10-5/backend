package com.codeit.project.slid_todo.application.auth.web.controller;

import com.codeit.project.slid_todo.application.auth.business.AuthFacade;
import com.codeit.project.slid_todo.application.auth.web.dto.SignupDto;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/api/user/signup")
    public ResponseEntity<ResponseDto<Void>> singup(
            @Valid @RequestBody SignupDto.Request requestDto) {

        authFacade.registerUser(requestDto);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
