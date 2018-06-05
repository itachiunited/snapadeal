package com.snapadeal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Product {

    private @Id @Generated String id;

    private String name;

    private String description;

    private String primaryImage;

    private List<String> additionalImages;

    private int totalQuantity;

    private int maxQuantityPerCustomer;

    private double listPrice;

    private double salePrice;

    private String discount;

    private List<String> tags;

    private String startTime;

    private String endTime;

    private long numberOfViews;

    private String publicImageId;

    private int reservationHoldTimeInHours;

    @DBRef
    @JsonBackReference
    private BusinessProfile businessProfile;

    public Product() {
        this.id = id;
        this.name = name;
        this.description = description;
        this.primaryImage = primaryImage;
        this.additionalImages = additionalImages;
        this.totalQuantity = totalQuantity;
        this.maxQuantityPerCustomer = maxQuantityPerCustomer;
        this.listPrice = listPrice;
        this.salePrice = salePrice;
        this.discount = discount;
        this.tags = tags;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfViews = numberOfViews;
        this.businessProfile = businessProfile;
        this.publicImageId = publicImageId;
        this.reservationHoldTimeInHours = reservationHoldTimeInHours;
    }

    public String getPublicImageId() {
        return publicImageId;
    }

    public void setPublicImageId(String publicImageId) {
        this.publicImageId = publicImageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public List<String> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<String> additionalImages) {
        this.additionalImages = additionalImages;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getMaxQuantityPerCustomer() {
        return maxQuantityPerCustomer;
    }

    public void setMaxQuantityPerCustomer(int maxQuantityPerCustomer) {
        this.maxQuantityPerCustomer = maxQuantityPerCustomer;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getDiscount() {
        return ((listPrice - salePrice)/listPrice)*100 + "%";
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(long numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public BusinessProfile getBusinessProfile() {
        return businessProfile;
    }

    public void setBusinessProfile(BusinessProfile businessProfile) {
        this.businessProfile = businessProfile;
    }

    public Timestamp getStartTime() {
        return Timestamp.valueOf(startTime);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return Timestamp.valueOf(endTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", primaryImage='" + primaryImage + '\'' +
                ", additionalImages=" + additionalImages +
                ", totalQuantity=" + totalQuantity +
                ", maxQuantityPerCustomer=" + maxQuantityPerCustomer +
                ", listPrice=" + listPrice +
                ", salePrice=" + salePrice +
                ", discount='" + discount + '\'' +
                ", tags=" + tags +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", numberOfViews=" + numberOfViews +
                ", publicImageId='" + publicImageId + '\'' +
                ", reservationHoldTimeInHours=" + reservationHoldTimeInHours +
                ", businessProfile=" + businessProfile +
                '}';
    }

    public int getReservationHoldTimeInHours() {
        return reservationHoldTimeInHours;
    }

    public void setReservationHoldTimeInHours(int reservationHoldTimeInHours) {
        this.reservationHoldTimeInHours = reservationHoldTimeInHours;
    }
}
