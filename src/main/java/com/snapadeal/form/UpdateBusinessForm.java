package com.snapadeal.form;

import com.snapadeal.entity.Address;
import com.snapadeal.entity.enums.Category;
import com.snapadeal.validators.ValidPassword;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class UpdateBusinessForm {

    @NotNull
    private String id;

    @NotNull(message = "Business Name must not be null")
    private String name;

    @NotNull(message = "Owner Name must not be null")
    private String businessOwnerName;

    private GeoJsonPoint location;

    @NotNull(message = "Phone Number must not be null")
    private String phoneNumber;

    @NotNull(message = "Description must not be null")
    private String description;

    private String logo;

    private String storeHours;

    private Address storeAddress;

    private String website;

    @Override
    public String toString() {
        return "BusinessProfile{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", businessOwnerName='" + businessOwnerName + '\'' +
                ", location=" + location +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", storeHours='" + storeHours + '\'' +
                ", storeAddress=" + storeAddress +
                ", website='" + website + '\'' +
                ", publicImageId='" + publicImageId + '\'' +
                ", category=" + category +
                '}';
    }

    public String getPublicImageId() {
        return publicImageId;
    }

    public void setPublicImageId(String publicImageId) {
        this.publicImageId = publicImageId;
    }

    private String publicImageId;

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
        if(storeAddress == null){
            storeAddress = new Address();
        }
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

    public UpdateBusinessForm() {
        this.businessOwnerName = businessOwnerName;
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.logo = logo;
        this.category = category;
        this.storeHours = storeHours;
        this.storeAddress = storeAddress;
        this.website = website;
        this.publicImageId = publicImageId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
