package com.snapadeal.repository;

import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String>
{
    public Product findByBusinessProfileId(String businessProfileId);
}
