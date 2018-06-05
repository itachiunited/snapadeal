package com.snapadeal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IP2DataLocation {
    private String geoname_id;
    private String country_flag_emoji_unicode;
    private String capital;
    private String country_flag;
    private String calling_code;
    private String is_eu;

    public String getGeoname_id ( ) {
        return geoname_id;
    }

    public void setGeoname_id ( String geoname_id ) {
        this.geoname_id = geoname_id;
    }

    public String getCountry_flag_emoji_unicode ( ) {
        return country_flag_emoji_unicode;
    }

    public void setCountry_flag_emoji_unicode ( String country_flag_emoji_unicode ) {
        this.country_flag_emoji_unicode = country_flag_emoji_unicode;
    }

    public String getCapital ( ) {
        return capital;
    }

    public void setCapital ( String capital ) {
        this.capital = capital;
    }

    public String getCountry_flag ( ) {
        return country_flag;
    }

    public void setCountry_flag ( String country_flag ) {
        this.country_flag = country_flag;
    }

    public String getCalling_code ( ) {
        return calling_code;
    }

    public void setCalling_code ( String calling_code ) {
        this.calling_code = calling_code;
    }

    public String getIs_eu ( ) {
        return is_eu;
    }

    public void setIs_eu ( String is_eu ) {
        this.is_eu = is_eu;
    }
}
