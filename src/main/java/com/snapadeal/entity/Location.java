package com.snapadeal.entity;


import lombok.Data;

import javax.persistence.Id;

@Data
public class Location {

    @Id
    private String id;

    private double[] position;

    private String pop;

    private String state;

    public String getId ( ) {
        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }

    public double[] getPosition ( ) {
        return position;
    }

    public void setPosition ( double[] position ) {
        this.position = position;
    }

    public String getPop ( ) {
        return pop;
    }

    public void setPop ( String pop ) {
        this.pop = pop;
    }

    public String getState ( ) {
        return state;
    }

    public void setState ( String state ) {
        this.state = state;
    }
}