package com.mandeep.zoogol.bean;

/**
 * Created by mandeep on 6/7/2016.
 */
public class YouTubeBeans {
    private String result;

    private String error;

    private Data[] data;

    public String getResult ()
    {
        return result;
    }

    public void setResult (String result)
    {
        this.result = result;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public Data[] getData ()
    {
        return data;
    }

    public void setData (Data[] data)
    {
        this.data = data;
    }


}
