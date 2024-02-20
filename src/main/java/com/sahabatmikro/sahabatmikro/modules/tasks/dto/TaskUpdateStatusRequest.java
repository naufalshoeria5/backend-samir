package com.sahabatmikro.sahabatmikro.modules.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskUpdateStatusRequest {
    @NotBlank
    private String taskId;

    @NotBlank
    private Boolean status;
}
