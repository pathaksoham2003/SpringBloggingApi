package com.soham.blog.service;

import com.soham.blog.config.EmailUtils;
import com.soham.blog.config.UserInfoDetails;
import com.soham.blog.dto.LoginUserRequest;
import com.soham.blog.dto.RegisterUserRequest;
import com.soham.blog.exception.RecordNotFoundException;
import com.soham.blog.exception.UserAlreadyRegisterException;
import com.soham.blog.jpa.AppUserRepository;
import com.soham.blog.model.User;
import com.soham.blog.utils.AppUtils;
import jakarta.mail.AuthenticationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private EmailUtils emailUtils;
    @Value("${spring.mail.username}")
    private String fromEmailId;
    @Value("${app.mail.verification-link}")
    private String mailVerificationLink;
    @Autowired
    private AppUtils appUtil;

    public User registerUser(RegisterUserRequest registerUserRequest) {
        User userTemp = userRepository.findByUsername(registerUserRequest.getUsername());
        if (!Objects.isNull(userTemp)) throw new UserAlreadyRegisterException("Email id already registered");
        User user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        BeanUtils.copyProperties(registerUserRequest, user);
        user.setRoles(registerUserRequest.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsAccountVerified(0);
        user.setIsSocialRegister(0);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setOtp(appUtil.getFourDigitNumber());
        User savedUser = userRepository.save(user);
        String emailVerificationLink = mailVerificationLink + user.getOtp() + savedUser.getUserId();
        emailUtils.sendMail(fromEmailId, registerUserRequest.getUsername(),
                "Regarding SapZap account verification.",
                "Click on below link to verify your email id : \n" + emailVerificationLink);
        log.info("Email verification link  : " + emailVerificationLink);
        return user;
    }

    public User login(LoginUserRequest loginUserRequest) throws  Exception{
        User userTemp = userRepository.findByUsername(loginUserRequest.getUsername());
        if(Objects.isNull(userTemp)) throw new RecordNotFoundException("Email id is not yet register.Please register and login again.");
        User user = userRepository.findByUsername(loginUserRequest.getUsername());
        if(user.getIsAccountVerified()==0 && loginUserRequest.getIsSocialRegister()==0) throw new
                AuthenticationFailedException("Your mail id verification is pending.Please check your mail box and verify your mail id.");
        return user;
    }

    public User verifyEmailId(Integer otp, String userId) {
        User user = userRepository.findByUserIdAndOtp(userId, otp);
        if (Objects.isNull(user)) {
            return null;
        } else {
            user.setIsAccountVerified(1);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("UserService:loadUserByUsername User not found with username : {}", username);
            throw new UsernameNotFoundException("could not found user...!!");
        }
        log.info("UserService:loadUserByUsername User found with username : {}", username);
        return new UserInfoDetails(user);
    }

}
