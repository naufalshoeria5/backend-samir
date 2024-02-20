package com.sahabatmikro.sahabatmikro.modules.tasks.service;

import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import com.sahabatmikro.sahabatmikro.modules.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void createTaskTest(){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setName("New Task");
        taskRequest.setNote("Note");
        taskRequest.setStatus(true);

        taskService.create(taskRequest);

        List<Task> all = taskRepository.findAll();

        Assertions.assertNotNull(all);
    }
}