package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceRequest {

    @Expose
    @SerializedName("userID")
    private int userID;

    @Expose
    @SerializedName("cariID")
    private int customerID;

    @Expose
    @SerializedName("yetkiID")
    private int privilege;

    @Expose
    @SerializedName("baslangic")
    private String baslangic;

    @Expose
    @SerializedName("bitis")
    private String bitis;

    public ServiceRequest() {
    }

    public ServiceRequest(int userID, int customerID,
                          int privilege, String baslangic, String bitis) {
        this.userID = userID;
        this.customerID = customerID;
        this.privilege = privilege;
        this.baslangic = baslangic;
        this.bitis = bitis;
    }

    public ServiceRequest(int userID, int privilege,
                          String baslangic, String bitis) {
        this.userID = userID;
        this.privilege = privilege;
        this.baslangic = baslangic;
        this.bitis = bitis;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getBaslangic() {
        return baslangic;
    }

    public void setBaslangic(String baslangic) {
        this.baslangic = baslangic;
    }

    public String getBitis() {
        return bitis;
    }

    public void setBitis(String bitis) {
        this.bitis = bitis;
    }
}
