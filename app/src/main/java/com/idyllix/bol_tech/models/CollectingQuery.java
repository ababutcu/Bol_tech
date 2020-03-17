package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollectingQuery {

    @Expose
    @SerializedName("kullaniciID")
    private int userID;
    @Expose
    @SerializedName("cariID")
    private int customerID;
    @Expose
    @SerializedName("baslangic")
    private String baslangic;
    @Expose
    @SerializedName("bitis")
    private String bitis;

    public CollectingQuery() {
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
