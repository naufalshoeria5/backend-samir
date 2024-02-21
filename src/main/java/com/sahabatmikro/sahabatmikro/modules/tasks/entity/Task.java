package com.sahabatmikro.sahabatmikro.modules.tasks.entity;

import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @Column(length = 100)
    String id;

    String name;

    String note;

    boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;
}
