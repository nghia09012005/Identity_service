package com.example.identity_service.Exception;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.identity_service.dto.request.*;

import java.util.Map;
import java.util.Objects;

// tap trung exception ve day
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final  String  MIN_ATTRIBUTE = "min";

    // Exception bat tac ca cac exception chua dc definde
    @ExceptionHandler(value = Exception.class)// bat exception
    ResponseEntity<ApiResponse> appExceptionHandler(Exception rte){

        ApiResponse apir = new ApiResponse<>();
        // gan error vao apir
        apir.setCode(error.UNCATEGORIZED_EXCEPTION.getCode());
        apir.setMessage(error.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity
                .status(error.UNCATEGORIZED_EXCEPTION.getHttpStatusCode())
                .body(apir); // lay message ra va respone
    }

    @ExceptionHandler(value = appException.class)// bat exception
    ResponseEntity<ApiResponse> appExceptionHandler(appException rte){
        error er = rte.getEr();//enum chua error
        ApiResponse apir = new ApiResponse<>();

        // gan error vao apir
        apir.setCode(er.getCode());
        apir.setMessage(er.getMessage());

        return ResponseEntity
                .status(er.getHttpStatusCode())
                .body(apir); // lay message ra va respone
    }

    @ExceptionHandler(value = AccessDeniedException.class)// accessdenied cua security
    ResponseEntity<ApiResponse> appExceptionHandler(AccessDeniedException exception){

        return ResponseEntity.status(error.UNAUTHORIZED.getHttpStatusCode())
                .body(
                        new ApiResponse().builder()
                                .code(error.UNAUTHORIZED.getCode())
                                .message(error.UNAUTHORIZED.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> passwordExceptionHandler(MethodArgumentNotValidException rte){
        String errorKey = rte.getFieldError().getDefaultMessage();

        error er = error.INVALID_KEY;
        Map<String,Object> attributes = null;
        try{
            er = error.valueOf(errorKey);
            var constraintViolation =
                    rte.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
        }
        catch (Exception e){

        }

        ApiResponse apir = new ApiResponse<>();

        // gan error vao apir
        apir.setCode(er.getCode());
        apir.setMessage(Objects.nonNull(attributes) ? mapAttribute(er.getMessage(),attributes) : er.getMessage());

        return ResponseEntity
                .status(er.getHttpStatusCode())
                .body(apir); // lay message ra va respone
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {

        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

}
