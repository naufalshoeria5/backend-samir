package com.sahabatmikro.sahabatmikro.modules.tasks.dto;


import com.sahabatmikro.sahabatmikro.helper.Status;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    String id;

    String name;

    String note;

    String status;
}
