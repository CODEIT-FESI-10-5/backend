package com.codeit.project.slid_todo.common.util;

import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private final ObjectMapper objectMapper;

    public void writeJsonResponse(HttpServletResponse response, ResponseDto<Void> body) throws IOException {
        setDefaultJsonResponseHeader(response, body);
        try (OutputStream out = response.getOutputStream()) {
            objectMapper.writeValue(out, body);
        }
    }

    public void setDefaultJsonResponseHeader(HttpServletResponse response, ResponseDto<Void> body) {
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(body.getHttpStatusCode());
    }
}
