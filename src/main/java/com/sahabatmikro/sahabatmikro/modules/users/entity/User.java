package com.sahabatmikro.sahabatmikro.modules.users.entity;

import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(length = 100)
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Nullable
    private String token;

    @Column(name = "token_expired_at")
    @Nullable
    private Long tokenExpiredAt;

    @OneToMany
    List<Task> lisTask;
}
