package com.soham.blog.service;

import com.soham.blog.dto.RegisterUserRequest;
import com.soham.blog.exception.UserAlreadyRegisterException;
import com.soham.blog.jpa.AppUserRepository;
import com.soham.blog.model.User;
import com.soham.blog.utils.AppUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppUtils appUtil;
    public User registerUser(RegisterUserRequest registerUserRequest) {
        User userTemp = userRepository.findByUserName(registerUserRequest.getUsername());
        if(!Objects.isNull(userTemp)) throw new UserAlreadyRegisterException("Email id already registered");
        User user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        BeanUtils.copyProperties(registerUserRequest, user);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsAccountVerified(0);
        user.setIsSocialRegister(0);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setOtp(appUtil.getFourDigitNumber());
        return userRepository.save(user);
    }

}
