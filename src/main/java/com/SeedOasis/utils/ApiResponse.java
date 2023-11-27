package com.SeedOasis.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

public class ApiResponse {

    public static <T> ApiResult<T> success(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String message, HttpStatus httpStatus) {
        return new ApiResult<>(false, null, new ApiError(message, httpStatus.value()));
    }

    @AllArgsConstructor
    @Data
    public static class ApiError {
        private final String message; //에러 메시지
        private final int status;
    }

    @AllArgsConstructor
    @Data
    public static class ApiResult<T> {
        private final boolean success;
        private final T response; //응답 data
        private final ApiError error;
    }
}
