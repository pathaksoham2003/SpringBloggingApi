package com.soham.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableCaching
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(@Autowired MongoTemplate mongoTemplate) {
//		return args -> {
//			if (!mongoTemplate.collectionExists("blogs")) {
//				mongoTemplate.createCollection("blogs");
//			}
//			if (!mongoTemplate.collectionExists("comments")) {
//				mongoTemplate.createCollection("comments");
//			}
//		};
//	}

}
