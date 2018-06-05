package com.snapadeal.services;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.snapadeal.constants.SnapADealConstants;
import com.snapadeal.entity.Location;
import com.snapadeal.entity.Product;
import com.snapadeal.form.LoginForm;
import com.snapadeal.form.ProductIntakeForm;
import com.snapadeal.repository.LocationRepository;
import com.snapadeal.repository.ProductRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import com.snapadeal.entity.BusinessProfile;
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
        product.setTotalQuantity(productIntakeForm.getTotalQuantity());
        product.setMaxQuantityPerCustomer(productIntakeForm.getMaxQuantityPerCustomer());
        product.setListPrice(productIntakeForm.getListPrice());
        product.setSalePrice(productIntakeForm.getSalePrice());

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
}
