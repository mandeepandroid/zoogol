package com.mandeep.zoogol.bean;

/**
 * Created by mandeep on 6/7/2016.
 */
public class Data {
    private String title;
    private String link;
    private String discription;
    private String id;
    private String category;
    private String image_name;
    private String sub_category;
    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }
    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }


    public String getImage_name ()
    {
        return image_name;
    }

    public void setImage_name (String image_name)
    {
        this.image_name = image_name;
    }

    public String getSub_category ()
    {
        return sub_category;
    }

    public void setSub_category (String sub_category)
    {
        this.sub_category = sub_category;
    }

      public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public String getDiscription ()
    {
        return discription;
    }

    public void setDiscription (String discription)
    {
        this.discription = discription;
    }
}
