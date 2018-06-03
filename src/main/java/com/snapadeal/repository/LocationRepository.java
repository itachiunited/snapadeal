package com.snapadeal.repository;

import com.snapadeal.entity.Location;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByPositionWithin ( Circle c );

    List<Location> findByPositionWithin(Box b);

    List<Location> findByPositionNear(Point p, Distance d);
}
