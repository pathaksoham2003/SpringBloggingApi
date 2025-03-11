package com.soham.blog.controller;

import com.soham.blog.config.JwtService;
import com.soham.blog.dto.DBSResponseEntity;
import com.soham.blog.dto.JwtResponse;
import com.soham.blog.dto.LoginUserRequest;
import com.soham.blog.dto.RegisterUserRequest;
import com.soham.blog.exception.AuthenticationFailedException;
import com.soham.blog.exception.RecordNotFoundException;
import com.soham.blog.exception.UserAlreadyRegisterException;
import com.soham.blog.model.User;
import com.soham.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/v1/register")
    public ResponseEntity<DBSResponseEntity<JwtResponse>> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        DBSResponseEntity<JwtResponse> dbsResponseEntity = new DBSResponseEntity<JwtResponse>();
        try{
            User user = userService.registerUser(registerUserRequest);
            dbsResponseEntity.setMessage("User registered successfully.");
            return ResponseEntity.ok(dbsResponseEntity);
        } catch (UserAlreadyRegisterException uae) {
            throw uae;
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/v1/verifyEmailId/{code}")
    public ResponseEntity<DBSResponseEntity<JwtResponse>> verifyEmailIdCall(@PathVariable String code) {
        DBSResponseEntity<JwtResponse> dbsResponseEntity = new DBSResponseEntity<>();
        try{
            User user = userService.verifyEmailId(Integer.parseInt(code.substring(0,4)),code.substring(4));
            if(Objects.isNull(user)) throw new RecordNotFoundException("User not found.");
            JwtResponse jwtResponse = new JwtResponse(jwtService.GenerateToken(user.getUsername()));
            dbsResponseEntity.setMessage("User verified successfully.");
            dbsResponseEntity.setData(jwtResponse);
            return ResponseEntity.ok(dbsResponseEntity);
        }catch (RecordNotFoundException exception) {
            log.debug("UserController:verifyEmailIdCall user not yet register : {}", exception);
            throw exception;
        } catch (AuthenticationFailedException exception) {
            log.debug("UserController:verifyEmailIdCall Authentication failed exception : {}", exception);
            throw exception;
        } catch (Exception exception) {
            log.debug("UserController:verifyEmailIdCall something when wrong : {}", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/v1/login")
    public ResponseEntity<DBSResponseEntity<JwtResponse>>login(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        DBSResponseEntity<JwtResponse> dbsResponseEntity = new DBSResponseEntity<JwtResponse>();
        try{
            User user = userService.login(loginUserRequest);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.getUsername(), loginUserRequest.getPassword()));
            if (!authentication.isAuthenticated()) throw new AuthenticationFailedException("Authentication failed.");
            JwtResponse jwtResponse = new JwtResponse(jwtService.GenerateToken(user.getUsername()));
            dbsResponseEntity.setMessage("User logged in successfully.");
            dbsResponseEntity.setData(jwtResponse);
            return ResponseEntity.ok(dbsResponseEntity);
        } catch (RecordNotFoundException rne) {
            throw rne;
        }catch(AuthenticationFailedException afe) {
            dbsResponseEntity.setData(null);
            dbsResponseEntity.setMessage(afe.getMessage());
            return ResponseEntity.ok(dbsResponseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
