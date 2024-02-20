package com.sahabatmikro.sahabatmikro.modules.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahabatmikro.sahabatmikro.helper.Utils;
import com.sahabatmikro.sahabatmikro.helper.WebResponse;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.RegisterRequest;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.TokenResponse;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import com.sahabatmikro.sahabatmikro.modules.users.repository.UserRepository;
import com.sahabatmikro.sahabatmikro.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception{
        RegisterRequest request = new RegisterRequest();
        request.setName("Asep");
        request.setUsername("asep");
        request.setPassword("asep");
        request.setEmail("asep@mail.com");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertEquals("OK",response.getData());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception{
        RegisterRequest request = new RegisterRequest();

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception{
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("asep");
        user.setName("Asep");
        user.setPassword(BCrypt.hashpw("asep",BCrypt.gensalt()));
        user.setEmail("asep@mail.com");

        userRepository.save(user);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("asep");
        request.setName("Asep");
        request.setPassword("asep");
        request.setEmail("asep@mail.com");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginSuccess() throws Exception{
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("asep");
        user.setName("Asep");
        user.setPassword(BCrypt.hashpw("asep",BCrypt.gensalt()));
        user.setEmail("asep@mail.com");

        userRepository.save(user);

        RegisterRequest request = new RegisterRequest();
        request.setName("Asep");
        request.setUsername("asep");
        request.setPassword("asep");
        request.setEmail("asep@mail.com");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            User userDb = userRepository.findFirstByUsername(request.getUsername()).orElse(null);
            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getTokenExpiredAt());

            assertNotNull(userDb);
            assertEquals(userDb.getToken(),response.getData().getToken());
            assertEquals(userDb.getTokenExpiredAt(),response.getData().getTokenExpiredAt());
        });
    }

    @Test
    void testLoginWrongPassword() throws Exception{
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("asep");
        user.setName("Asep");
        user.setPassword(BCrypt.hashpw("asep",BCrypt.gensalt()));
        user.setEmail("asep@mail.com");

        userRepository.save(user);

        RegisterRequest request = new RegisterRequest();
        request.setName("Asep");
        request.setUsername("asep");
        request.setPassword("password salah");
        request.setEmail("asep@mail.com");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginUndefinedUsername() throws Exception{
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("asep");
        user.setName("Asep");
        user.setPassword(BCrypt.hashpw("asep",BCrypt.gensalt()));
        user.setEmail("asep@mail.com");

        userRepository.save(user);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("ujang");
        request.setName("Asep");
        request.setPassword("asep");
        request.setEmail("asep@mail.com");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("asep");
        user.setName("Asep");
        user.setPassword(BCrypt.hashpw("asep",BCrypt.gensalt()));
        user.setEmail("asep@mail.com");
        user.setToken("token");
        user.setTokenExpiredAt(Utils.next30Days());

        User save = userRepository.save(user);

        log.info("user 233 {}", save.getToken());

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","token")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("OK",response.getData());

            User userDb = userRepository.findById(user.getUsername()).orElse(null);

            assertNotNull(userDb);
            assertNull(userDb.getTokenExpiredAt());
            assertNull(userDb.getToken());
        });
    }


}