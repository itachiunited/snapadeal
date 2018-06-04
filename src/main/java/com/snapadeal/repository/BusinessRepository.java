package com.snapadeal.repository;

import com.snapadeal.entity.BusinessProfile;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BusinessRepository extends MongoRepository<BusinessProfile, String>
{
    public BusinessProfile findByLogin(String login);

    public List<BusinessProfile> findByLocationNear(Point p, Distance distance);
}
