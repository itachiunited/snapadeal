package com.snapadeal.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ProductIntakeForm {

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Description must not be null")
    private String description;

    @NotNull(message = "Image must not be null")
    private MultipartFile primaryImage;

    private List<String> additionalImages;

    @NotNull(message = "Total Quantity must not be null")
    private int totalQuantity;

    @NotNull(message = "Max Quantity must not be null")
    private int maxQuantityPerCustomer;

    @NotNull(message = "List Price must not be null")
    private double listPrice;

    @NotNull(message = "Sale Price must not be null")
    private double salePrice;

    private String tags;

    @NotNull(message = "Start Time must not be null")
    private String startTime;

    @NotNull(message = "End Time must not be null")
    private String endTime;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductIntakeForm{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", primaryImage=" + primaryImage +
                ", additionalImages=" + additionalImages +
                ", totalQuantity=" + totalQuantity +
                ", maxQuantityPerCustomer=" + maxQuantityPerCustomer +
                ", listPrice=" + listPrice +
                ", salePrice=" + salePrice +
                ", tags='" + tags + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public ProductIntakeForm() {
        this.name = name;
        this.description = description;
        this.primaryImage = primaryImage;
        this.additionalImages = additionalImages;
        this.totalQuantity = totalQuantity;
        this.maxQuantityPerCustomer = maxQuantityPerCustomer;
        this.listPrice = listPrice;
        this.salePrice = salePrice;
        this.tags = tags;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public MultipartFile getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(MultipartFile primaryImage) {
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
