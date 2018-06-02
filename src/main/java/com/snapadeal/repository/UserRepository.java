package com.snapadeal.repository;

import com.snapadeal.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>
{
    public User findByLogin(String login);
}
