package com.sahabatmikro.sahabatmikro.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String type;
    private final String message;
}
