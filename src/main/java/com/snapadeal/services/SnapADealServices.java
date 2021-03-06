package com.snapadeal.services;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.snapadeal.constants.SnapADealConstants;
import com.snapadeal.entity.*;
import com.snapadeal.form.LoginForm;
import com.snapadeal.form.ProductIntakeForm;
import com.snapadeal.repository.LocationRepository;
import com.snapadeal.repository.ProductRepository;
import com.snapadeal.repository.ReservationOrderRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import com.snapadeal.exceptions.BusinessProfileException;
import com.snapadeal.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SnapADealServices implements SnapADealConstants{

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private MongoConverter mongoConverter;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReservationOrderRepository reservationOrderRepository;

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

    public List<Location> findLocationByMiles(Point dus){
        List<Location> locations = repo.findByPositionNear(dus , new Distance(25, Metrics.MILES) );
        return locations;
    }

    public IP2Data findIP2LocaitonData(String ip){
        RestTemplate restTemplate = new RestTemplate();
        IP2Data      ip2Data        = restTemplate.getForObject ( "http://api.ipstack.com/"+ip+"?access_key=616160d52abbf0e20f53d38dc7528f99", IP2Data.class );
        return ip2Data;
    }

    public void loadLocationToSession (HttpServletRequest pRequest ){
            String  ip              = pRequest.getHeader ( "X-FORWARDED-FOR" );
            if (ip == null || "".equals(ip)) {
                ip = pRequest.getRemoteAddr();
            }
            System.out.println ("Ip Address " + ip );
            boolean isIp2DataLoaded = false;
            if ( null != ip ) {
                IP2Data ip2Data = findIP2LocaitonData ( ip );
                if ( null != ip2Data && null != ip2Data.getZip ( ) ) {
                    httpSession.setAttribute ( "latitude" , ip2Data.getLatitude ( ) );
                    httpSession.setAttribute ( "longitude" , ip2Data.getLongitude ( ) );
                    httpSession.setAttribute ( "zipCode" , ip2Data.getZip ( ) );
                    System.out.println ( ip2Data.toString ( ) );
                    isIp2DataLoaded = true;
                }
            }

            if ( ! isIp2DataLoaded && null != pRequest.getParameter ( "latitude" ) ) {
                httpSession.setAttribute ( "latitude" , pRequest.getParameter ( "latitude" ) );
                httpSession.setAttribute ( "longitude" , pRequest.getParameter ( "longitude" ) );
                double            latitude  = Double.parseDouble ( pRequest.getParameter ( "latitude" ) );
                double            longitude = Double.parseDouble ( pRequest.getParameter ( "longitude" ) );
                Point             dus       = new Point ( longitude , latitude );
                List < Location > locations = findLocationByMiles ( dus );
                if ( null != locations && locations.size ( ) > 0 ) {
                    Location location = locations.get ( 0 );
                    System.out.println ( location );
                    httpSession.setAttribute ( "zipCode" , location.getId ( ) );
                }
            }
    }

    public void findZipCodeForLatLong(double latitude,double longitude){
        Point             dus       = new Point ( longitude , latitude );
        List < Location > locations = findLocationByMiles ( dus );
        if ( null != locations && locations.size()>0) {
            Location location = locations.get ( 0 );
            System.out.println ( location );
            httpSession.setAttribute ( "zipCode" , location.getId ( ) );
        }
    }

    public void setSessionContextForBusinessUser(String pLogin, String pPassword, BusinessProfile businessProfile){
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add (new SimpleGrantedAuthority("BUSINESSUSER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(pLogin, pPassword, grantedAuthorities));
        httpSession.setAttribute(CURRENT_USER_KEY, businessProfile);
        httpSession.setAttribute(USER_TYPE_KEY, BUSINESSUSER);
    }

    public BusinessProfile loginBusinessAccount(LoginForm loginForm) throws BusinessProfileException {
        BusinessProfile bpFromRep = businessRepository.findByLogin(loginForm.getLogin());

        if(null == bpFromRep)
        {
            throw new BusinessProfileException("Please enter a valid Email Address");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(null!=loginForm.getPassword() && passwordEncoder.matches(loginForm.getPassword(),bpFromRep.getPassword()))
        {
            setSessionContextForBusinessUser (bpFromRep.getLogin(), bpFromRep.getPassword(), bpFromRep);
            return bpFromRep;
        }
        else
        {
            throw new BusinessProfileException("Please enter valid credentials. Email Address or Password is not valid");
        }
    }

    public BusinessProfile getCurrentBusinessUser(boolean forceFresh) {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("SnapADealServices : getCurrentBusinessUser() : Login --> "+login);
        if(BUSINESSUSER.equalsIgnoreCase((String)httpSession.getAttribute(USER_TYPE_KEY))) {
            BusinessProfile businessProfile = (BusinessProfile) httpSession.getAttribute(CURRENT_USER_KEY);
            if (forceFresh || null == businessProfile) {
                businessProfile = businessRepository.findByLogin(login);
                httpSession.setAttribute(CURRENT_USER_KEY, businessProfile);
            }
            return businessProfile;
        }
        else {
            httpSession.invalidate();
            BusinessProfile businessProfile = new BusinessProfile();
            setSessionContextForBusinessUser (businessProfile.getLogin(), businessProfile.getPassword(), businessProfile);
            httpSession.setAttribute(USER_TYPE_KEY, BUSINESSUSER);
            return businessProfile;
        }

    }

    public void createProduct(BusinessProfile businessProfile, Product product) {

        product.setBusinessProfile(businessProfile);
        product.setCreationTime(String.valueOf(new Timestamp(System.currentTimeMillis())));
        productRepository.save(product);

        List<Product> products = businessProfile.getProductList();

        if(null==products)
        {
            products = new ArrayList<>();
        }
        products.add(product);

        businessProfile.setProductList(products);

        DBObject update = getDbObject(businessProfile);
        mongoTemplate.updateFirst(query(where("id").is(businessProfile.getId())), Update.fromDocument(new Document("$set", update)).push("events", businessProfile), BusinessProfile.class);
    }

    private DBObject getDbObject(Object o) {
        BasicDBObject basicDBObject = new BasicDBObject();
        mongoConverter.write(o, basicDBObject);
        return basicDBObject;
    }

    public Product convertFormToProduct(@Valid ProductIntakeForm productIntakeForm) {
        Product product = new Product();

        product.setName(productIntakeForm.getName());
        product.setDescription(productIntakeForm.getDescription());
        product.setTotalQuantity(Integer.parseInt(productIntakeForm.getTotalQuantity()));
        product.setMaxQuantityPerCustomer(Integer.parseInt(productIntakeForm.getMaxQuantityPerCustomer()));
        product.setListPrice(Double.parseDouble(productIntakeForm.getListPrice()));
        product.setSalePrice(Double.parseDouble(productIntakeForm.getSalePrice()));

        if(null!=productIntakeForm.getTags()) {
            List<String> tagList = new ArrayList<String>(Arrays.asList(productIntakeForm.getTags().split(",")));
            product.setTags(tagList);
        }

        if(null!=productIntakeForm.getStartTime() && null==productIntakeForm.getId())
        {
            String tempString = productIntakeForm.getStartTime().replace("T"," ");
            tempString = tempString + ":00";

            product.setStartTime(tempString);
        }

        if(null!=productIntakeForm.getEndTime() && null==productIntakeForm.getId())
        {
            String tempString = productIntakeForm.getEndTime().replace("T"," ");
            tempString = tempString + ":00";

            product.setEndTime(tempString);
        }

        if(null!=productIntakeForm.getPrimaryImage() && !productIntakeForm.getPrimaryImage().isEmpty()) {
            System.out.println("IMAGE STARTS");
            try {
                File imageFile = new File(productIntakeForm.getPrimaryImage().getOriginalFilename());

                imageFile.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                fileOutputStream.write(productIntakeForm.getPrimaryImage().getBytes());
                fileOutputStream.close();

                Map<String, String> imageUploadResult = imageService.updateImage(imageFile);

                System.out.println("Public ID --> "+imageUploadResult.get("public_id"));
                System.out.println("Image URL --> "+imageUploadResult.get("url"));

                product.setPublicImageId(imageUploadResult.get("public_id"));
                product.setPrimaryImage(imageUploadResult.get("url"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(null!=productIntakeForm.getId())
        {
            product.setId(productIntakeForm.getId());
        }

        return product;
    }

    public List<Product> getAllNearByProducts(double latitude, double longitude, double distance) {
        double[] coords = new double[2];
        coords[0]=latitude;
        coords[1]=longitude;
        List<BusinessProfile> businessProfilesList = businessRepository.findByLocationNear(new Point(longitude,latitude),new Distance(distance,Metrics.MILES));


        List<Product> listOfProducts = new ArrayList<>();

        for(BusinessProfile bp : businessProfilesList)
        {
            if(null != bp.getProductList() && bp.getProductList().size() > 0)
            listOfProducts.addAll(bp.getProductList());
        }
        return listOfProducts;
    }

    public Product findProductById(String productId) {
        return productRepository.findById(productId).get();
    }

    public void editProduct(Product product) {

        DBObject update = getDbObject(product);
        mongoTemplate.updateFirst(query(where("id").is(product.getId())), Update.fromDocument(new Document("$set", update)).push("events", product), Product.class);

        System.out.println("Product --> "+product);

    }

    public ReservationOrder placeOrder(ReservationOrder reservationOrder) {

        Product product = findProductById(reservationOrder.getProductId());

        reservationOrder.setBusinessId(product.getBusinessProfile().getId());

        // Random Code Generation

        String reservationCode = String.valueOf((long)Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

        while(null!=reservationOrderRepository.findByReservationCode(reservationCode))
        {
            reservationCode = String.valueOf((long)Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
        }
        System.out.println("Reservation Code For this Order --> "+reservationCode);

        reservationOrder.setReservationCode(reservationCode);
        reservationOrder.setSubmittedDate(new Timestamp(System.currentTimeMillis()));

        int hours = 48;
        if(0!=product.getReservationHoldTimeInHours())
        {
            hours = product.getReservationHoldTimeInHours();
        }

        reservationOrder.setReservationEndDate(new Timestamp(System.currentTimeMillis()+(hours*60*60*1000)));

        reservationOrderRepository.save(reservationOrder);
        System.out.println("Reservation Order --> "+reservationOrder.toString());

        return reservationOrder;
    }

    public void updateBusinessProfile(BusinessProfile pBusinessProfile) {

        DBObject update = getDbObject(pBusinessProfile);
        mongoTemplate.updateFirst(query(where("id").is(pBusinessProfile.getId())), Update.fromDocument(new Document("$set", update)).push("events", pBusinessProfile), BusinessProfile.class);

        System.out.println("BusinessProfile --> "+pBusinessProfile);

    }


    public BusinessProfile updatePassword(BusinessProfile businessProfile) throws BusinessProfileException {

        BusinessProfile vBusinessProfile = null;

        if(!StringUtils.isEmpty(businessProfile.getLogin())){
            vBusinessProfile = businessRepository.findByLogin(businessProfile.getLogin());
        }else if(!StringUtils.isEmpty(businessProfile.getId())){
            vBusinessProfile = businessRepository.findById(businessProfile.getId()).get();
        }

        if(vBusinessProfile != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            vBusinessProfile.setPassword(passwordEncoder.encode(businessProfile.getPassword()));

            updateBusinessProfile(vBusinessProfile);

            setSessionContextForBusinessUser(vBusinessProfile.getLogin(), vBusinessProfile.getPassword(), vBusinessProfile);

            System.out.println("SnapADealServices:createBusinessAccount() - Business Profile Created for User --> " + businessProfile.getLogin() + " with ID --> " + businessProfile.getId());
        }
        else {
            throw new BusinessProfileException("Account Unavailable for the login specified");
        }
        return businessProfile;
    }
}
