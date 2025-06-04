package com.example.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL) // bao cho json bo cac field null
public class ApiResponse<T> {
    @Builder.Default
    int code = 1000; // 1000: success
    String message;
     T result ;


}
