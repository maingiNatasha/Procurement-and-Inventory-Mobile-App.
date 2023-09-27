package com.example.prototype;

public class HomeItem {
    private String item_name;
    private int ID_image;

    public HomeItem(String item_name, int ID_image) {
        this.item_name = item_name;
        this.ID_image = ID_image;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setID_image(int ID_image) {
        this.ID_image = ID_image;
    }

    public int getID_image() {
        return ID_image;
    }
}
