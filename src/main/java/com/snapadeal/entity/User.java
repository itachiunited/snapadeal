package com.snapadeal.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Id;

@Data
public class User {

    private @Id @Generated String id;

    private String login;

    private String firstName;

    private String lastName;

    private String password;

    private String location; // If Required

    private String latitude;

    private String longitude;

    private String phoneNumber;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
               ", latitude='" + latitude + '\'' +
               ", longitude='" + longitude + '\'' +
                '}';
    }

    public User() {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.latitude=latitude;
        this.longitude=longitude;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLatitude ( ) {
        return latitude;
    }

    public void setLatitude ( String latitude ) {
        this.latitude = latitude;
    }

    public String getLongitude ( ) {
        return longitude;
    }

    public void setLongitude ( String longitude ) {
        this.longitude = longitude;
    }
}
