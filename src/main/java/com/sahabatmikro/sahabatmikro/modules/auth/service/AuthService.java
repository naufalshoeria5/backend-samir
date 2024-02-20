package com.sahabatmikro.sahabatmikro.modules.auth.service;

import com.sahabatmikro.sahabatmikro.helper.Utils;
import com.sahabatmikro.sahabatmikro.helper.ValidationService;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.LoginRequest;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.RegisterRequest;
import com.sahabatmikro.sahabatmikro.modules.auth.dto.TokenResponse;
import com.sahabatmikro.sahabatmikro.modules.users.entity.User;
import com.sahabatmikro.sahabatmikro.modules.users.repository.UserRepository;
import com.sahabatmikro.sahabatmikro.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final ValidationService validationService;

    public AuthService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    /**
     * Register Service
     * @param request
     * description
     */
    @Transactional
    public void register(RegisterRequest request){
        validationService.validate(request);

        if (userRepository.findFirstByUsername(request.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username exist");

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));

        userRepository.save(user);
    }

    /**
     * Login Service
     * @param request
     * description
     * @return Token Response
     */
    @Transactional
    public TokenResponse login(LoginRequest request){
        validationService.validate(request);

        User user = userRepository.findFirstByUsername(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username Or Password Wrong"));

        if (!BCrypt.checkpw(request.getPassword(),user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username Or Password Wrong");

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(Utils.next30Days());

        userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getToken())
                .tokenExpiredAt(user.getTokenExpiredAt())
                .build();
    }

    /**
     * Logout Service
     * @param user
     * description
     */
    @Transactional
    public void logout(User user){
        log.info("User 87 : {}", user);
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }
}
