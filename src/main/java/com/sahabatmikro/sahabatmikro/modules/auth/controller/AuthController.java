package com.sahabatmikro.sahabatmikro.modules.auth.controller;

import com.sahabatmikro.sahabatmikro.helper.WebResponse;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.LoginRequest;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.RegisterRequest;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.TokenResponse;
import com.sahabatmikro.sahabatmikro.modules.auth.service.AuthService;
import com.sahabatmikro.sahabatmikro.modules.users.dto.UserResponse;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import com.sahabatmikro.sahabatmikro.modules.users.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * create register
     * @param request
     * description
     * @return Web Response
     */
    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@RequestBody RegisterRequest request){
        authService.register(request);

        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }

    /**
     * login
     * @param request
     * description
     * @return Web Response
     *
     */
    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginRequest request){
        TokenResponse response = authService.login(request);

        return WebResponse.<TokenResponse>builder()
                .data(response)
                .build();
    }

    /**
     * get current user
     * @param user
     * description
     * @return
     */
    @GetMapping(
            path = "/current"
    )
    public UserResponse getCurrent(User user){

        return userService.get(user);
    }


    /**
     * logout
     * @param user
     * description
     * @return web response
     */
    @DeleteMapping(
            path = "/logout",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout(User user){
        authService.logout(user);

        return WebResponse.<String>builder()
                .data("OK")
                .build();
    }
}
