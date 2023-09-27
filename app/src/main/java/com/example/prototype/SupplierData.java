package com.example.prototype;

public class SupplierData {
    String supplierName, supplierItemName, supplierItemDescription, itemId;
    int supplierItemPrice;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierItemName() {
        return supplierItemName;
    }

    public void setSupplierItemName(String supplierItemName) {
        this.supplierItemName = supplierItemName;
    }

    public int getSupplierItemPrice() {
        return supplierItemPrice;
    }

    public void setSupplierItemPrice(int supplierItemPrice) {
        this.supplierItemPrice = supplierItemPrice;
    }

    public String getSupplierItemDescription() {
        return supplierItemDescription;
    }

    public void setSupplierItemDescription(String supplierItemDescription) {
        this.supplierItemDescription = supplierItemDescription;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
