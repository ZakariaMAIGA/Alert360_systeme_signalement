package com.alert360.controller.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class APIResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime date;

    public APIResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.date = LocalDateTime.now();
    }
}