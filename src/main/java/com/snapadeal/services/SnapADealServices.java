package com.snapadeal.services;
import com.snapadeal.entity.Location;
import com.snapadeal.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SnapADealServices {

    private static final Point DUS = new Point( -72.936114, 42.182949 );

    @Autowired
    LocationRepository repo;

    public void findLocationByMiles(){
        List<Location> locations = repo.findByPositionNear(DUS , new Distance(25, Metrics.MILES) );
        System.out.println (locations );
    }

}
