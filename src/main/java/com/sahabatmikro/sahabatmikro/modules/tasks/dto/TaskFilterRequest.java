package com.sahabatmikro.sahabatmikro.modules.tasks.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskFilterRequest {
    private String name;

    private String note;

    private Boolean status;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
