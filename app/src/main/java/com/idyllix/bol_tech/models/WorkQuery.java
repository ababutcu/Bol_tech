package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkQuery {

    @Expose
    @SerializedName("kullaniciID")
    private int userID;
    @Expose
    @SerializedName("yetki")
    private int yetki;
    @Expose
    @SerializedName("baslangic")
    private String baslangic;
    @Expose
    @SerializedName("bitis")
    private String bitis;

    public WorkQuery() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getYetki() {
        return yetki;
    }

    public void setYetki(int yetki) {
        this.yetki = yetki;
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
