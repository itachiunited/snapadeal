package com.snapadeal.services;
import com.snapadeal.constants.SnapADealConstants;
import com.snapadeal.entity.Location;
import com.snapadeal.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.exceptions.BusinessProfileException;
import com.snapadeal.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class SnapADealServices implements SnapADealConstants{

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private HttpSession httpSession;

    public BusinessProfile createBusinessAccount(BusinessProfile businessProfile) throws BusinessProfileException {

        if(null == businessRepository.findByLogin(businessProfile.getLogin()))
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            businessProfile.setPassword(passwordEncoder.encode(businessProfile.getPassword()));

            businessRepository.save(businessProfile);

            setSessionContextForBusinessUser(businessProfile.getLogin(), businessProfile.getPassword(), businessProfile);

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

    public void setSessionContextForBusinessUser(String pLogin, String pPassword, BusinessProfile businessProfile){
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add (new SimpleGrantedAuthority("BUSINESSUSER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(pLogin, pPassword, grantedAuthorities));
        httpSession.setAttribute(CURRENT_USER_KEY, businessProfile);
        httpSession.setAttribute(USER_TYPE_KEY, BUSINESSUSER);
    }
}
