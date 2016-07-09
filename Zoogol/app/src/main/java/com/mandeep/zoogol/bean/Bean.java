package com.mandeep.zoogol.bean;

import java.util.ArrayList;

/**
 * Created by pc on 3/4/2016.
 */
public class Bean {


    private String str_name, str_link;
    private int placeholder;
    private ArrayList<Bean_sub_category> al_subcategory;

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }



    public String getStr_name() {
        return str_name;
    }
    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }
    public String getStr_link() {
        return str_link;
    }
    public void setStr_link(String str_link) {
        this.str_link = str_link;
    }
    public ArrayList<Bean_sub_category> getAl_subcategory() {
        return al_subcategory;
    }
    public void setAl_subcategory(ArrayList<Bean_sub_category> al_subcategory) {
        this.al_subcategory = al_subcategory;
    }
}
