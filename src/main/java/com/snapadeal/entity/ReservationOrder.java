package com.snapadeal.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class ReservationOrder {

    private @Id @Generated String id;

    private String reservationCode;

    private String productId;

    @NotEmpty(message = "Quantity must not be null")
    private String quantity;

    @NotEmpty(message = "Email must not be null")
    private String email;

    @NotEmpty(message = "Phone Number must not be null")
    private String phoneNumber;

    private String profileId;

    private Timestamp submittedDate;

    private Timestamp reservationEndDate;

    private String businessId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Timestamp getReservationEndDate() {
        return reservationEndDate;
    }

    public void setReservationEndDate(Timestamp reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "ReservationOrder{" +
                "id='" + id + '\'' +
                ", reservationCode='" + reservationCode + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileId='" + profileId + '\'' +
                ", submittedDate=" + submittedDate +
                ", reservationEndDate=" + reservationEndDate +
                ", businessId='" + businessId + '\'' +
                '}';
    }

    public ReservationOrder() {
        this.id = id;
        this.reservationCode = reservationCode;
        this.productId = productId;
        this.quantity = quantity;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileId = profileId;
        this.submittedDate = submittedDate;
        this.reservationEndDate = reservationEndDate;
        this.businessId = businessId;
    }
}
