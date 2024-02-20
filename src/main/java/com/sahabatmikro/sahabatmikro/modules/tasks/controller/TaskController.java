package com.sahabatmikro.sahabatmikro.modules.tasks.controller;

import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskResponse;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskUpdateStatusRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import com.sahabatmikro.sahabatmikro.modules.tasks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    /**
     * get all list
     * @return
     */
    @GetMapping
    public List<Task> get(){
        return taskService.getAll();
    }

    /**
     * find by id
     * @param taskId
     * @return
     */
    @GetMapping(
            params = "/taskId"
    )
    public TaskResponse getById(@RequestParam String taskId){
        return taskService.findById(taskId);
    }

    /**
     * create new task
     * @param request
     * @return
     */
    @PostMapping
    public String createTask(@RequestBody TaskRequest request){
        taskService.create(request);

        return "OK";
    }

    /**
     * update status task
     * @param request
     * @return
     */
    @PatchMapping(
            params = "taskId"
    )
    public String statusUpdate(@RequestBody TaskUpdateStatusRequest request){
        taskService.updateStatus(request);

        return "OK";
    }

    /**
     * delete task by id
     * @param taskId
     * @return
     */
    @DeleteMapping()
    public String deleteTask(@RequestParam String taskId){
        taskService.delete(taskId);

        return "OK";
    }
}
