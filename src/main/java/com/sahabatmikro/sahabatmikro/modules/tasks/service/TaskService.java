package com.sahabatmikro.sahabatmikro.modules.tasks.service;

import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskResponse;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskUpdateStatusRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import com.sahabatmikro.sahabatmikro.modules.tasks.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    /**
     * get all data task
     */
    @Transactional(readOnly = true)
    public List<Task> getAll(){
        return taskRepository.findAll();
    }

    /**
     * create new task
     * @param request
     */
    @Transactional()
    public void create(TaskRequest request){
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName(request.getName());
        task.setNote(request.getNote());
        task.setStatus(request.isStatus());

        taskRepository.save(task);
    }

    /**
     *
     * @param taskId
     * @return
     */
    @Transactional(readOnly = true)
    public TaskResponse findById(String taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
            new RuntimeException("Task Not Found")
        );

        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .note(task.getNote())
                .status(task.isStatus() ? "COMPLETED" : "UNCOMPLETED")
                .build();
    }

    /**
     *
     * update status completed task
     * @param request
     */
    @Transactional
    public void updateStatus(TaskUpdateStatusRequest request){
        Task task = taskRepository.findById(request.getTaskId()).orElseThrow(() ->
                new RuntimeException("Task Not Found")
        );

        task.setStatus(request.getStatus());

        taskRepository.save(task);
    }

    /**
     * delete task
     * @param taskId
     */
    @Transactional
    public void delete(String taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new RuntimeException("Task Not Found")
        );

        taskRepository.delete(task);
    }
}
