package com.sahabatmikro.sahabatmikro.modules.tasks.controller;

import com.sahabatmikro.sahabatmikro.helper.PagingResponse;
import com.sahabatmikro.sahabatmikro.helper.WebResponse;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.*;
import com.sahabatmikro.sahabatmikro.modules.tasks.service.TaskService;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    /**
     *
     * @param user
     * current user
     * @param name
     * search name
     * @param status
     * filter by status
     * @param page
     * number of page
     * @param size
     * page size
     * @return Web Response
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TaskResponse>> get(User user,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "status", required = false) Boolean status,
                                               @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
        TaskFilterRequest taskFilterRequest = TaskFilterRequest.builder()
                .name(name)
                .status(status)
                .page(page)
                .size(size)
                .build();

        Page<TaskResponse> all = taskService.getAll(user, taskFilterRequest);

        return WebResponse.<List<TaskResponse>>builder()
                .data(all.getContent())
                .pagingResponse(PagingResponse.builder()
                        .currentPage(all.getNumber())
                        .totalPage(all.getTotalPages())
                        .size(all.getSize())
                        .build())
                .build();
    }

    /**
     * find by id
     * @param taskId
     * path task id
     * @return web response
     */
    @GetMapping(
            path = "/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> getById(User user,@PathVariable String taskId){
        return WebResponse.<TaskResponse>builder()
                .data(taskService.findById(user,taskId))
                .build();
    }

    /**
     * create new task
     * @param request
     * Task Request
     * @return web response
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> createTask(User user, @RequestBody TaskCreateRequest request){
        TaskResponse taskResponse = taskService.create(user, request);

        return WebResponse.<TaskResponse>builder()
                .data(taskResponse)
                .build();
    }

    /**
     *
     * @param user
     * current user
     * @param request
     * task update status
     * @return Web Response
     */
    @PatchMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> statusUpdate(User user, @RequestBody TaskUpdateStatusRequest request){
        TaskResponse taskResponse = taskService.updateStatus(user, request);

        return WebResponse.<TaskResponse>builder()
                .data(taskResponse)
                .build();
    }

    /**
     *
     * @param user
     * current user
     * @param request
     * r
     * @return web response
     */
    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> update(User user,@RequestBody TaskUpdateRequest request){
        TaskResponse update = taskService.update(user, request);

        return WebResponse.<TaskResponse>builder().data(update).build();
    }

    /**
     * delete task by id
     *
     * @param taskId task id for delete
     * @return string
     */
    @DeleteMapping(
            path = "{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Object> deleteTask(User user, @PathVariable String taskId){
        taskService.delete(user, taskId);

        return WebResponse.builder().data("OK").build();
    }
}
