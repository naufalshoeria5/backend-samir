package com.sahabatmikro.sahabatmikro.modules.tasks.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahabatmikro.sahabatmikro.helper.Utils;
import com.sahabatmikro.sahabatmikro.helper.WebResponse;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskCreateRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskResponse;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskUpdateRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.dto.TaskUpdateStatusRequest;
import com.sahabatmikro.sahabatmikro.modules.tasks.entity.Task;
import com.sahabatmikro.sahabatmikro.modules.tasks.repository.TaskRepository;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import com.sahabatmikro.sahabatmikro.modules.users.repository.UserRepository;
import com.sahabatmikro.sahabatmikro.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User defaultUser;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Asep");
        user.setUsername("asep");
        user.setPassword(BCrypt.hashpw("asep",BCrypt.gensalt()));
        user.setToken("asep-token");
        user.setTokenExpiredAt(Utils.next30Days());

        userRepository.save(user);


        defaultUser = user;
    }

    @Test
    void createTaskTestSuccess() throws Exception {
        TaskCreateRequest taskRequest = new TaskCreateRequest();
        taskRequest.setName("New Task");
        taskRequest.setNote("Note");
        taskRequest.setStatus(true);

        mockMvc.perform(
                post("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .header("X-API-TOKEN","asep-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            Assertions.assertNull(response.getErrors());
            assertEquals(taskRequest.getName(),response.getData().getName());
            assertEquals(taskRequest.getNote(),response.getData().getNote());


            assertTrue(taskRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void createTaskTestBadRequest() throws Exception {
        TaskCreateRequest taskRequest = new TaskCreateRequest();
        taskRequest.setName("");
        taskRequest.setNote("Note");
        taskRequest.setStatus(true);

        mockMvc.perform(
                post("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .header("X-API-TOKEN","asep-token")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void getTestTaskByIdSuccess() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName("Task 1");
        task.setNote("Task 2");
        task.setStatus(true);
        task.setUser(defaultUser);

        taskRepository.save(task);

        mockMvc.perform(
                get("/api/tasks/"+ task.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","asep-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNull(response.getErrors());

            assertEquals(response.getData().getId(), task.getId());
            assertEquals(response.getData().getName(), task.getName());
            assertEquals(response.getData().getNote(), task.getNote());
        });
    }
    @Test
    void getTestTaskByIdFailed() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName("Task 1");
        task.setNote("Task 2");
        task.setStatus(true);
        task.setUser(defaultUser);

        taskRepository.save(task);

        mockMvc.perform(
                get("/api/tasks/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","asep-token")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void taskUpdateStatusSuccess() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName("Task 1");
        task.setNote("note");
        task.setStatus(true);
        task.setUser(defaultUser);

        taskRepository.save(task);

        TaskUpdateStatusRequest statusRequest = new TaskUpdateStatusRequest();
        statusRequest.setId(task.getId());
        statusRequest.setStatus(false);

        mockMvc.perform(
                patch("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","asep-token")
                        .content(objectMapper.writeValueAsString(statusRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNull(response.getErrors());
            log.info(response.getData().getStatus());
            assertEquals("UNCOMPLETED",response.getData().getStatus());
        });
    }
    @Test
    void taskUpdateSuccess() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName("Task 1");
        task.setNote("note");
        task.setStatus(true);
        task.setUser(defaultUser);

        taskRepository.save(task);

        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
        taskUpdateRequest.setId(task.getId());
        taskUpdateRequest.setName("Task Update");
        taskUpdateRequest.setNote("note Update");
        taskUpdateRequest.setStatus(false);

        mockMvc.perform(
                put("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "asep-token")
                        .content(objectMapper.writeValueAsString(taskUpdateRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(taskUpdateRequest.getName(), response.getData().getName());
            assertEquals(taskUpdateRequest.getNote(), response.getData().getNote());
            assertEquals("UNCOMPLETED", response.getData().getStatus());
        });
    }

    @Test
    void taskDeleteSuccess() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName("Task 1");
        task.setNote("note");
        task.setStatus(true);
        task.setUser(defaultUser);

        taskRepository.save(task);

        mockMvc.perform(
                delete("/api/tasks/" + task.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","asep-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
            assertNull(response.getErrors());
            assertEquals("OK", response.getData());

            assertFalse(taskRepository.existsById(task.getId()));
        });
    }
    @Test
    void taskNotfound() throws Exception {
        mockMvc.perform(
                get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","asep-token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<TaskResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertEquals(0,response.getData().size());
            assertEquals(10,response.getPagingResponse().getSize());
            assertEquals(0,response.getPagingResponse().getCurrentPage());
            assertEquals(0,response.getPagingResponse().getTotalPage());
        });
    }

    @Test
    void taskFilterStatusSuccess() throws Exception {
        List<Task> taskList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Task task = new Task();

            task.setId(UUID.randomUUID().toString());
            task.setName("Task " + i);
            task.setNote("note");
            task.setStatus(i % 2 == 0);
            task.setUser(defaultUser);
            taskList.add(task);
        }
        taskRepository.saveAll(taskList);

        mockMvc.perform(
                get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","asep-token")
                        .queryParam("status","true")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<TaskResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertEquals(5,response.getData().size());
            assertEquals(10,response.getPagingResponse().getSize());
            assertEquals(0,response.getPagingResponse().getCurrentPage());
            assertEquals(1,response.getPagingResponse().getTotalPage());
        });
    }

}