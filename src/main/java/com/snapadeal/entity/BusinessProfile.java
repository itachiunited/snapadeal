package com.snapadeal.entity;

import com.snapadeal.entity.enums.Category;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.List;

@Data
public class BusinessProfile {

    private @Id @Generated String id;

    private String login;

    private String name;

    private String businessOwnerName;

    private String password;

    private GeoJsonPoint location;

    private String phoneNumber;

    private String description;

    private String logo;

    private String displayName;

    private String storeHours;

    private Address storeAddress;

    private String website;

    @Override
    public String toString() {
        return "BusinessProfile{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", businessOwnerName='" + businessOwnerName + '\'' +
                ", password='" + password + '\'' +
                ", location=" + location +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", displayName='" + displayName + '\'' +
                ", storeHours='" + storeHours + '\'' +
                ", storeAddress=" + storeAddress +
                ", website='" + website + '\'' +
                ", category=" + category +
                ", productList=" + productList +
                '}';
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStoreHours() {
        return storeHours;
    }

    public void setStoreHours(String storeHours) {
        this.storeHours = storeHours;
    }

    public Address getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(Address mailingAddress) {
        this.storeAddress = storeAddress;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Enumerated(EnumType.STRING)
    private Category category;

    @DBRef
    private List<Product> productList;

    public BusinessProfile() {
        this.id = id;
        this.login = login;
        this.businessOwnerName = businessOwnerName;
        this.name = name;
        this.password = password;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.logo = logo;
        this.displayName = displayName;
        this.productList = productList;
        this.category = category;
        this.storeHours = storeHours;
        this.storeAddress = storeAddress;
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessOwnerName() {
        return businessOwnerName;
    }

    public void setBusinessOwnerName(String businessOwnerName) {
        this.businessOwnerName = businessOwnerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
