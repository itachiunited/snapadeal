package com.snapadeal.repository;

import com.snapadeal.entity.BusinessProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessRepository extends MongoRepository<BusinessProfile, String>
{
    public BusinessProfile findByLogin(String login);
}
