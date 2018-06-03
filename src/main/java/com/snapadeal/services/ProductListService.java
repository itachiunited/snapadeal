package com.snapadeal.services;

import com.snapadeal.entity.BusinessProfile;
import com.snapadeal.entity.Product;
import com.snapadeal.entity.enums.Category;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


@Service
public class ProductListService {

    private BusinessProfile getBizProfileBasedOnEmail(String emailAddress)
    {
        BusinessProfile businessProfile = new BusinessProfile();


        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(new Point(123,456));
        businessProfile.setLocation(geoJsonPoint);

        businessProfile.setBusinessOwnerName(emailAddress);
        businessProfile.setCategory(Category.FOOD_AND_BEVERAGE);
        businessProfile.setDescription("Indian Rest.");
        businessProfile.setId("123");
        businessProfile.setLogo("adsfa");
        businessProfile.setName(emailAddress);
        businessProfile.setPhoneNumber("234456789");
        Product product = new Product();
        product.setBusinessProfile(businessProfile);
        product.setDescription("an Indian dish made with highly seasoned rice and meat, fish, or vegetables.");
        product.setDisplayName("Authentic Hyderabad Biryani ");
        product.setListPrice(4.00);
        product.setMaxQuantityPerCustomer(5);
        product.setPrimaryImage("https://res.cloudinary.com/dlbwgbupa/image/upload/v1527986380/i3w3ftbqzubrghntbofb.jpg");
        product.setSalePrice(2.50);
        product.setTotalQuantity(10);
        ArrayList<Product>  products = new ArrayList<Product>();
        products.add(product);
        businessProfile.setProductList(products);
        return businessProfile;
    }


    public ArrayList<Product> getProductsToDisplay()
    {
        ArrayList<Product> products = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            BusinessProfile businessProfile = getBizProfileBasedOnEmail("sample@gmail.com"+i);
            products.addAll(businessProfile.getProductList());
        }
        return products;
    }
}
