package com.sahabatmikro.sahabatmikro.modules.users.entity;

import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotNull
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @NotNull
    @Size(max = 120)
    private String password;

    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    @OneToMany(mappedBy = "user")
    List<Task> tasks;
}
