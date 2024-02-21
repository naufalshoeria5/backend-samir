package com.sahabatmikro.sahabatmikro.modules.tasks.repository;

import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {

    Optional<Task> findFirstByUserAndId(User user,String taskId);

}
