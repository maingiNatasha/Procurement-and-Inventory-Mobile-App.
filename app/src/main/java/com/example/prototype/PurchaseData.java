package com.example.prototype;

public class PurchaseData {
    String pOrderId, pItemName, pItemUnit, paymentMethod, pSupplier, pCustomer, pStatus;
    int pItemQuantity,  pItemTotal;

    public String getpOrderId() {
        return pOrderId;
    }

    public void setpOrderId(String pOrderId) {
        this.pOrderId = pOrderId;
    }

    public String getpItemName() {
        return pItemName;
    }

    public void setpItemName(String pItemName) {
        this.pItemName = pItemName;
    }

    public String getpItemUnit() {
        return pItemUnit;
    }

    public void setpItemUnit(String pItemUnit) {
        this.pItemUnit = pItemUnit;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getpSupplier() {
        return pSupplier;
    }

    public void setpSupplier(String pSupplier) {
        this.pSupplier = pSupplier;
    }

    public int getpItemQuantity() {
        return pItemQuantity;
    }

    public void setpItemQuantity(int pItemQuantity) {
        this.pItemQuantity = pItemQuantity;
    }

    public int getpItemTotal() {
        return pItemTotal;
    }

    public void setpItemTotal(int pItemTotal) {
        this.pItemTotal = pItemTotal;
    }

    public String getpCustomer() {
        return pCustomer;
    }

    public void setpCustomer(String pCustomer) {
        this.pCustomer = pCustomer;
    }

    public String getpStatus() {
        return pStatus;
    }

    public void setpStatus(String pStatus) {
        this.pStatus = pStatus;
    }
}
