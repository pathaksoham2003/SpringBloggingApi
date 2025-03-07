package com.soham.blog.exception;

import com.soham.blog.dto.DBSResponseEntity;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Logging Error : https://stackoverflow.com/questions/79274106/how-to-use-both-restcontrolleradvice-and-swagger-ui-in-spring-boot
@Slf4j
@Hidden
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DBSResponseEntity> handlingValidationExcetion (MethodArgumentNotValidException methodArgumentNotValidException) {
        String errorMessage  = methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage();
        DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
        dbsResponseEntity.setMessage(errorMessage);
        return new ResponseEntity<>(dbsResponseEntity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<DBSResponseEntity> handlingRecordNotFoundExcetion (RecordNotFoundException recordNotFoundException) {
        DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
        dbsResponseEntity.setMessage(recordNotFoundException.getMessage());
        return new ResponseEntity<>(dbsResponseEntity, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<DBSResponseEntity> handlindUserAlreadyRegisterException(UserAlreadyRegisterException userAlreadyRegisterException) {
        DBSResponseEntity dbsResponseEntity = new DBSResponseEntity();
        dbsResponseEntity.setMessage(userAlreadyRegisterException.getMessage());
        return new ResponseEntity<>(dbsResponseEntity, HttpStatus.BAD_REQUEST);
    }
}
