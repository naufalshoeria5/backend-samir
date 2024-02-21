package com.sahabatmikro.sahabatmikro.modules.users.service;

import com.sahabatmikro.sahabatmikro.helper.ValidationService;
import com.sahabatmikro.sahabatmikro.modules.users.dto.UserResponse;
import com.sahabatmikro.sahabatmikro.modules.users.dto.UserUpdateRequest;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import com.sahabatmikro.sahabatmikro.modules.users.repository.UserRepository;
import com.sahabatmikro.sahabatmikro.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    /**
     *
     * @param user
     * get from header
     * @return user response
     */
    public UserResponse get(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


    /**
     *
     * @param user
     * get from header
     * @param request
     * get from user update
     * @return user response
     */
    public UserResponse update(User user, UserUpdateRequest request){
        if (!request.getName().isBlank()) user.setName(request.getName());

        if (!request.getEmail().isBlank()) user.setEmail(request.getEmail());

        if (!request.getPassword().isBlank()) user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        User userUpdate = userRepository.save(user);

        return UserResponse.builder()
                .id(userUpdate.getId())
                .username(userUpdate.getUsername())
                .name(userUpdate.getName())
                .email(userUpdate.getEmail())
                .build();
    }
}
