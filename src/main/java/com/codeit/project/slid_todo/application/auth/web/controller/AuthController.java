package com.codeit.project.slid_todo.application.auth.web.controller;

import com.codeit.project.slid_todo.application.auth.business.AuthFacade;
import com.codeit.project.slid_todo.application.auth.web.dto.LoginRequestDto;
import com.codeit.project.slid_todo.application.auth.web.dto.LoginResponseDto;
import com.codeit.project.slid_todo.application.auth.web.dto.SignupDto;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "회원가입 및 토큰 재발급 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/api/user/signup")
    @Operation(summary = "회원가입", description = "사용자 정보를 받아 회원가입을 수행합니다.")
    public ResponseEntity<ResponseDto<Void>> singup(
            @Valid @RequestBody SignupDto.Request requestDto) {

        authFacade.registerUser(requestDto);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/api/auth/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    public ResponseEntity<ResponseDto<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto response = authFacade.login(requestDto);
        ResponseDto<LoginResponseDto> responseDto = ResponseDto.<LoginResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/auth/reissue")
    @Operation(summary = "토큰 재발급", description = "Access Token이 만료되었을 때 Refresh Token으로 재발급합니다.")
    public ResponseEntity<ResponseDto<Void>> reissue(HttpServletRequest request, HttpServletResponse response) {
        authFacade.reissue(request,response);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(responseDto.getHttpStatusCode()).body(responseDto);
    }
}
