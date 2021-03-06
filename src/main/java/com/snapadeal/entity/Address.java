package com.snapadeal.entity;

import lombok.Generated;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

public class Address {

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String zip;

    private String country;

    public Address() {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    @Override
    public String toString() {
        return "MailingAddress{" +
                "addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public String getAddressString(){
        String vAddress = "";

        if(addressLine1 != null){
            vAddress = addressLine1 + ",";
        }
        if(addressLine2 != null){
            vAddress = vAddress + addressLine2 + ",";
        }
        if(city != null){
            vAddress = vAddress + city + ",";
        }
        if(state != null){
            vAddress = vAddress + state + " ";
        }
        if(zip != null){
            vAddress = vAddress + zip;
        }
        return vAddress;
    }
}
