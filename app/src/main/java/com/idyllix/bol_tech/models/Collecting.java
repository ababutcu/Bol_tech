package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collecting {

    /*
            public Int32 kullaniciID { get; set; }
            public Int32 cariID { get; set; }
            public String odemeMiktari { get; set; }
            public String tahsilatTarihi { get; set; }
            public String aciklama { get; set; }
     */

    @Expose
    @SerializedName("ID")
    private int id;

    @Expose
    @SerializedName("kullaniciID")
    private int userID;

    @Expose
    @SerializedName("adiSoyadi")
    private String userName;

    @Expose
    @SerializedName("cariAdi")
    private String customerName;

    @Expose
    @SerializedName("cariID")
    private int cariID;

    @Expose
    @SerializedName("odemeMiktari")
    private String odemeMiktari;

    @Expose
    @SerializedName("tahsilatTarihi")
    private String date;

    @Expose
    @SerializedName("aciklama")
    private String aciklama;

    public Collecting() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCariID() {
        return cariID;
    }

    public void setCariID(int cariID) {
        this.cariID = cariID;
    }

    public String getOdemeMiktari() {
        return odemeMiktari;
    }

    public void setOdemeMiktari(String odemeMiktari) {
        this.odemeMiktari = odemeMiktari;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
