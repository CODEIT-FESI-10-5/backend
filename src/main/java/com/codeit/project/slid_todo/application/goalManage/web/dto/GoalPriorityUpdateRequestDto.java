package com.codeit.project.slid_todo.application.goalManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "목표 투두 우선순위 수정 요청")
public class GoalPriorityUpdateRequestDto {

    @NotBlank(message = "우선순위 순서는 필수입니다.")
    @Pattern(regexp = "^\\d+(,\\d+)*$", message = "우선순위는 숫자와 쉼표로만 구성되어야 합니다. (예: 1,2,3,4,5)")
    @Schema(description = "투두 우선순위 순서 (쉼표로 구분된 투두 ID)", example = "1,2,5,4,3,6,7,8,9,10")
    private String priorityOrder;
} 