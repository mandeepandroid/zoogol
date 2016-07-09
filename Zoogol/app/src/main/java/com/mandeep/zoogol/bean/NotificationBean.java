package com.mandeep.zoogol.bean;

/**
 * Created by mandeep on 6/5/2016.
 */
public class NotificationBean {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZkey() {
        return zkey;
    }

    public void setZkey(String zkey) {
        this.zkey = zkey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReadstatus() {
        return readstatus;
    }

    public void setReadstatus(String readstatus) {
        this.readstatus = readstatus;
    }

    public String getNewnoti() {
        return newnoti;
    }

    public void setNewnoti(String newnoti) {
        this.newnoti = newnoti;
    }

    private String id, zkey, message, status, readstatus, newnoti;

}
