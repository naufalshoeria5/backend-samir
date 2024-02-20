package com.sahabatmikro.sahabatmikro.modules.users.service;

import com.sahabatmikro.sahabatmikro.modules.users.dto.UserResponse;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import com.sahabatmikro.sahabatmikro.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    /**
     *
     * @param user
     * @return
     */
    public UserResponse get(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
