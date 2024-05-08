package com.ersted.userservices.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {
    private String status;
    private String message;
}
