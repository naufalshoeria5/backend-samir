package com.sahabatmikro.sahabatmikro.modules.tasks.service;

import com.sahabatmikro.sahabatmikro.helper.ValidationService;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.*;
import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import com.sahabatmikro.sahabatmikro.modules.tasks.repository.TaskRepository;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ValidationService validationService;

    public static final String CONSTANT_TASK_NOT_FOUND = "Task Not Found";
    public static final String CONSTANT_COMPLETED = "COMPLETED";
    public static final String CONSTANT_UNCOMPLETED = "UNCOMPLETED";

    /**
     * get all data task
     */
    @Transactional(readOnly = true)
    public Page<TaskResponse> getAll(User user, TaskFilterRequest filterRequest){
        Specification<Task> specification = (root, query, builder) -> {
              List<Predicate> predicates = new ArrayList<>();
              predicates.add(builder.equal(root.get("user"),user));
              if (Objects.nonNull(filterRequest.getName())) predicates.add(builder.like(root.get("name"), "%" + filterRequest.getName() + "%"));
              if (Objects.nonNull(filterRequest.getNote())) predicates.add(builder.like(root.get("note"), "%" + filterRequest.getName() + "%"));
              if (Objects.nonNull(filterRequest.getStatus())) predicates.add(builder.equal(root.get("status"), filterRequest.getStatus()));

              return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());

        Page<Task> tasks = taskRepository.findAll(specification, pageable);

        List<TaskResponse> taskResponses = tasks.getContent().stream().map(this::toTaskResponse).toList();
        return new PageImpl<>(taskResponses, pageable,tasks.getTotalElements());
    }

    /**
     * method create user
     * @param user
     * entity user
     * @param request
     * task request
     */
    @Transactional
        public TaskResponse create(User user, TaskCreateRequest request){
        validationService.validate(request);

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName(request.getName());
        task.setNote(request.getNote());
        task.setStatus(request.isStatus());
        task.setUser(user);

        taskRepository.save(task);

        return this.toTaskResponse(task);
    }


    /**
     *
     * @param user
     * user login
     * @param taskId
     * task id
     * @return task response
     */
    @Transactional(readOnly = true)
    public TaskResponse findById(User user,String taskId){
        Task task = taskRepository.findFirstByUserAndId(user,taskId).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND,CONSTANT_TASK_NOT_FOUND)
        );

        return this.toTaskResponse(task);
    }

    /**
     *
     * update status completed task
     * @param request
     * Task Update Status Request
     */
    @Transactional
    public TaskResponse updateStatus(User user,TaskUpdateStatusRequest request){
        Task task = taskRepository.findFirstByUserAndId(user,request.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, CONSTANT_TASK_NOT_FOUND)
        );

        task.setStatus(request.getStatus());

        taskRepository.save(task);

        return this.toTaskResponse(task);
    }

    /**
     * Task Service
     * @param user
     * user login
     * @param request
     * Task Update Request
     * @return Task Response
     */
    @Transactional
    public TaskResponse update(User user, TaskUpdateRequest request){
        Task task = taskRepository.findFirstByUserAndId(user, request.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, CONSTANT_TASK_NOT_FOUND)
        );
        task.setName(request.getName());
        task.setNote(request.getNote());
        task.setStatus(request.isStatus());

        Task save = taskRepository.save(task);

        return this.toTaskResponse(save);
    }

    /**
     * delete task
     * @param taskId
     * task id for delete
     * @param user
     * current user
     */
    @Transactional
    public void delete(User user, String taskId){
        Task task = taskRepository.findFirstByUserAndId(user, taskId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, CONSTANT_TASK_NOT_FOUND)
        );

        taskRepository.delete(task);
    }


    /**
     * task response
     * @param task
     * task entity
     * @return Task Response
     */
    private TaskResponse toTaskResponse(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .note(task.getNote())
                .status(task.isStatus() ? CONSTANT_COMPLETED : CONSTANT_UNCOMPLETED)
                .build();
    }
}
