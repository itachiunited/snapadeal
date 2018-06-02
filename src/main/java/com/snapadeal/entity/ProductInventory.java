package com.snapadeal.entity;

import lombok.Data;

@Data
public class ProductInventory {

    private String id;

    private int availableQty;

    private int reservedQty;

    private int soldQty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public int getReservedQty() {
        return reservedQty;
    }

    public void setReservedQty(int reservedQty) {
        this.reservedQty = reservedQty;
    }

    public int getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(int soldQty) {
        this.soldQty = soldQty;
    }

    @Override
    public String toString() {
        return "ProductInventory{" +
                "id='" + id + '\'' +
                ", availableQty=" + availableQty +
                ", reservedQty=" + reservedQty +
                ", soldQty=" + soldQty +
                '}';
    }

    public ProductInventory() {
        this.id = id;
        this.availableQty = availableQty;
        this.reservedQty = reservedQty;
        this.soldQty = soldQty;
    }
}
