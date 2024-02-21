package com.banking.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApiResponse<T> {
    private Boolean status;
    private String message;
    private T data;

    public ApiResponse<T> success(T payload) {
        this.setData(payload);
        this.setStatus(true);
        this.setMessage("successful");
        return this;
    }

    public ApiResponse<T> success(T payload, String message) {
        this.setData(payload);
        this.setStatus(true);
        this.setMessage(message);
        return this;
    }

    public ApiResponse<T> failure(T payload) {
        this.setData(payload);
        this.setStatus(false);
        this.setMessage("failed");
        return this;
    }

    public ApiResponse<T> failure(T payload, String reason) {
        this.setData(payload);
        this.setStatus(false);
        this.setMessage(reason);
        return this;
    }

    public ApiResponse<T> failure(String reason) {
        this.setStatus(false);
        this.setMessage(reason);
        return this;
    }
}
