package com.snapadeal.repository;

import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.entity.ReservationOrder;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservationOrderRepository extends MongoRepository<ReservationOrder, String>
{
    public ReservationOrder findByProfileId(String profileId);

    public ReservationOrder findByReservationCode(String reservationCode);
}
