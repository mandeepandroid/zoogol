package com.mandeep.zoogol.bean;

/**
 * Created by mandeep on 3/29/2016.
 */
public class InstoreDataBean {

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private String city, image;
    private int quantity;

}
