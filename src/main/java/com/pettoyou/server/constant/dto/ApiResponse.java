package com.pettoyou.server.constant.dto;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiResponse<T> {
    private int httpStatusCode;
    private String code;
    private String message;
    private T data;

    // HospitalControllerAdmin
    public static <T> ResponseEntity<ApiResponse<T>> createSuccessWithOk(T data) {
        return ResponseEntity.ok()
                .body(
                        new ApiResponse<>(
                                HttpStatus.OK.value(),
                                CustomResponseStatus.SUCCESS.getCode(),
                                CustomResponseStatus.SUCCESS.getMessage(),
                                data)
                );
    }

    public static <T> ResponseEntity<ApiResponse<T>> createSuccessWithCreated(T data, URI location) {
        return ResponseEntity.created(location)
                .body(
                        new ApiResponse<>(
                                HttpStatus.OK.value(),
                                CustomResponseStatus.SUCCESS.getCode(),
                                CustomResponseStatus.SUCCESS.getMessage(),
                                data)
                );
    }

    /***
     * @param bindingResult : @Valid 의 유효성 검사를 실패한 값(필드)들
     * @return : HttpStatus 와 Code, Message, 오류 데이터를 반환한다.
     */
    public static ApiResponse<Map<String, String>> createInValid(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }

        return new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                CustomResponseStatus.INVALID_ERROR.getCode(),
                CustomResponseStatus.INVALID_ERROR.getMessage(),
                errors
        );
    }

    /***
     * 예외처리를 위한 메서드
     * @param status : Custom Status Code
     * @return : data 없이 ApiResponse 를 반환한다.
     */
    public static ApiResponse<String> createError(CustomResponseStatus status) {
        return new ApiResponse<>(status.getHttpStatusCode(), status.getCode(), status.getMessage(), null);
    }
}
