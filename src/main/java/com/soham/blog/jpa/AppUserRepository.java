package com.soham.blog.jpa;

import com.soham.blog.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends MongoRepository<User,String> {
    User findByUserNameAndPassword(String userName, String password);

    User findByUserName(String userName);

    User findByUserId(String userId,Integer otp);
}
