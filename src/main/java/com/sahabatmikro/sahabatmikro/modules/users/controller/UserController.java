package com.sahabatmikro.sahabatmikro.modules.users.controller;

import com.sahabatmikro.sahabatmikro.helper.WebResponse;
import com.sahabatmikro.sahabatmikro.modules.users.dto.UserResponse;
import com.sahabatmikro.sahabatmikro.modules.users.dto.UserUpdateRequest;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import com.sahabatmikro.sahabatmikro.modules.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * get current user login
     * @param user
     * description
     * @return web response
     */
    @GetMapping(
            path = "/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> current(User user){
        UserResponse userResponse = userService.get(user);

        return WebResponse.<UserResponse>builder()
                .data(userResponse)
                .build();
    }

    /**
     * update user
     * @param user
     * from header
     * @param request
     * user update request
     * @return web response
     */
    @PatchMapping(
            path = "/update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user, @RequestBody UserUpdateRequest request){
        UserResponse update = userService.update(user, request);

        return WebResponse.<UserResponse>builder()
                .data(update)
                .build();
    }
}
