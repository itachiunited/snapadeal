package com.snapadeal.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ProductIntakeForm {

    @NotEmpty(message = "Please enter a valid Name")
    private String name;

    @NotEmpty(message = "Please enter a valid Description")
    private String description;

    @NotNull(message = "Please upload a valid Image")
    private MultipartFile primaryImage;

    private List<String> additionalImages;

    @NotEmpty(message = "Please enter a valid Total Quantity")
    private String totalQuantity;

    @NotEmpty(message = "Please enter a valid Max Quantity per Customer")
    private String maxQuantityPerCustomer;

    @NotEmpty(message = "Please enter a valid List Price")
    private String listPrice;

    @NotEmpty(message = "Please enter a valid Sale Price")
    private String salePrice;

    private String tags;

    @NotEmpty(message = "Please enter a valid Start Time")
    private String startTime;

    @NotEmpty(message = "Please enter a valid End Time")
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

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getMaxQuantityPerCustomer() {
        return maxQuantityPerCustomer;
    }

    public void setMaxQuantityPerCustomer(String maxQuantityPerCustomer) {
        this.maxQuantityPerCustomer = maxQuantityPerCustomer;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
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
