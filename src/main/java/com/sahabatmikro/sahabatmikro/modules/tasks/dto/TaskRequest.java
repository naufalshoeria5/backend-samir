package com.sahabatmikro.sahabatmikro.modules.tasks.dto;

import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    String id;

    String name;

    String note;

    boolean status;
}
