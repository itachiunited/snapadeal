package com.snapadeal.services;
import com.snapadeal.entity.Location;
import com.snapadeal.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;

import java.util.List;

import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.exceptions.BusinessProfileException;
import com.snapadeal.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SnapADealServices {

    @Autowired
    private BusinessRepository businessRepository;

    public BusinessProfile createBusinessAccount(BusinessProfile businessProfile) throws BusinessProfileException {

        if(null == businessRepository.findByLogin(businessProfile.getLogin()))
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            businessProfile.setPassword(passwordEncoder.encode(businessProfile.getPassword()));

            businessRepository.save(businessProfile);

            System.out.println("SnapADealServices:createBusinessAccount() - Business Profile Created for User --> "+businessProfile.getLogin()+" with ID --> "+businessProfile.getId());
        }
        else {
            throw new BusinessProfileException("Account already available. Please sign in");
        }
        return businessProfile;
    }

    private static final Point DUS = new Point( -72.936114, 42.182949 );

    @Autowired
    LocationRepository repo;

    public void findLocationByMiles(){
        List<Location> locations = repo.findByPositionNear(DUS , new Distance(25, Metrics.MILES) );
        System.out.println (locations );
    }

}
