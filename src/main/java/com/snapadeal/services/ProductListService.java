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





    private BusinessProfile getBizProfile(String name,String displayName,String imageUrl,
                                                      double listPrice, double salePrice,String description)
    {
        BusinessProfile businessProfile = new BusinessProfile();


        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(new Point(123,456));
        businessProfile.setLocation(geoJsonPoint);
        businessProfile.setBusinessOwnerName(name);
        businessProfile.setCategory(Category.FOOD_AND_BEVERAGE);
        businessProfile.setDescription(name);
        businessProfile.setId("123");
        businessProfile.setLogo("adsfa");
        businessProfile.setName(name);
        businessProfile.setPhoneNumber("234456789");
        Product product = new Product();
        product.setBusinessProfileId(businessProfile.getId());
        product.setDescription(description);
        product.setName(displayName);
        product.setListPrice(listPrice);
        product.setMaxQuantityPerCustomer(5);
        product.setPrimaryImage(imageUrl);
        product.setSalePrice(salePrice);
        product.setTotalQuantity(10);
        ArrayList<Product>  products = new ArrayList<Product>();
        products.add(product);
        businessProfile.setProductList(products);
        return businessProfile;
    }


    public ArrayList<Product> getProductsToDisplay()
    {
        ArrayList<Product> products = new ArrayList<>();
        BusinessProfile businessProfile = getBizProfile("Headliner Salon","Mens Hair Hut","http://www.headlinerssalon.net/yahoo_site_admin/assets/images/P1242038.75201143_std.JPG",44.00,20.00,
                "We offer the finest in service for hair, waxing and ear piercing.  We are a  Paul Mitchell Signature Salon, and a Scruples Integrity Salon, utilizing and promoting the best professional products from Paul Mitchell, Scruples, Matrix, and Keratin Complex.");
            products.addAll(businessProfile.getProductList());
        BusinessProfile   businessProfile2 = getBizProfile("Blackberry Farm","Spa Treatment","https://media.timeout.com/images/102832374/image.jpg",2344.00,1020.00,"Southern Sorghum Beautifying Body Wrap (60mins, $185), a farm-to-treatment-table body wrap showcasing sorghum, a gluten-free grain rich in vitamins and minerals that Southern farmers have been harvesting for centuries.");
        products.addAll(businessProfile2 .getProductList());
        BusinessProfile businessProfile3 = getBizProfile("SenSpa","SenZen Body Ritual","https://media.timeout.com/images/102829142/750/562/image.jpg",344.00,120.00,"SenZen Body Ritual (60mins, $150), which begins with a bamboo and Thai citrus body scrub and concludes with an application of the spa’s tea-based SenZen body moisturizer.");
        products.addAll(businessProfile3 .getProductList());
        return products;
    }
}
