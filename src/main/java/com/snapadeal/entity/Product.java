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

    private String displayName;

    private String description;

    private String primaryImage;

    private List<String> additionalImages;

    private int totalQuantity;

    private int maxQuantityPerCustomer;

    private double listPrice;

    private double salePrice;

    private String discount;

    private List<String> tags;

    private Timestamp startTime;

    private Timestamp endTime;

    private long numberOfViews;

    @DBRef
    @JsonBackReference
    private BusinessProfile businessProfile;

    public Product() {
        this.id = id;
        this.displayName = displayName;
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
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", primaryImage='" + primaryImage + '\'' +
                ", additionalImages=" + additionalImages +
                ", totalQuantity=" + totalQuantity +
                ", maxQuantityPerCustomer=" + maxQuantityPerCustomer +
                ", listPrice=" + listPrice +
                ", salePrice=" + salePrice +
                ", discount='" + discount + '\'' +
                ", tags=" + tags +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", numberOfViews=" + numberOfViews +
                ", businessProfile=" + businessProfile +
                '}';
    }
}
