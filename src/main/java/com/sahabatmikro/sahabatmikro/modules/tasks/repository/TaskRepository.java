package com.sahabatmikro.sahabatmikro.modules.tasks.repository;

import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {
}
