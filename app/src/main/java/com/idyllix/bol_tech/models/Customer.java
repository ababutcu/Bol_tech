package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {

    @Expose
    @SerializedName("ID")
    private int id;

    @Expose
    @SerializedName("adi")
    private String customerName;

    @Expose
    @SerializedName("unvani")
    private String customerUnvani;

    @Expose
    @SerializedName("yetkiliAdi")
    private String yetkiliAdi;

    @Expose
    @SerializedName("yetkiliTelefon")
    private String yetkiliTelefon;

    @Expose
    @SerializedName("cariTelefon")
    private String cariTelefon;

    @Expose
    @SerializedName("cariEmail")
    private String cariEmail;

    @Expose
    @SerializedName("bakiye")
    private String bakiye;

    public Customer() {
    }

    public Customer(int id, String customerName, String customerUnvani) {
        this.id = id;
        this.customerName = customerName;
        this.customerUnvani = customerUnvani;
    }

    public Customer(int id, String customerName, String customerUnvani,
                    String yetkiliAdi, String yetkiliTelefon, String cariTelefon, String cariEmail) {
        this.id = id;
        this.customerName = customerName;
        this.customerUnvani = customerUnvani;
        this.yetkiliAdi = yetkiliAdi;
        this.yetkiliTelefon = yetkiliTelefon;
        this.cariTelefon = cariTelefon;
        this.cariEmail = cariEmail;
    }

    public Customer(int id, String customerName, String customerUnvani,
                    String yetkiliAdi, String yetkiliTelefon, String cariTelefon,
                    String cariEmail, String bakiye) {
        this.id = id;
        this.customerName = customerName;
        this.customerUnvani = customerUnvani;
        this.yetkiliAdi = yetkiliAdi;
        this.yetkiliTelefon = yetkiliTelefon;
        this.cariTelefon = cariTelefon;
        this.cariEmail = cariEmail;
        this.bakiye = bakiye;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerUnvani() {
        return customerUnvani;
    }

    public void setCustomerUnvani(String customerUnvani) {
        this.customerUnvani = customerUnvani;
    }

    public String getYetkiliAdi() {
        return yetkiliAdi;
    }

    public void setYetkiliAdi(String yetkiliAdi) {
        this.yetkiliAdi = yetkiliAdi;
    }

    public String getYetkiliTelefon() {
        return yetkiliTelefon;
    }

    public void setYetkiliTelefon(String yetkiliTelefon) {
        this.yetkiliTelefon = yetkiliTelefon;
    }

    public String getCariTelefon() {
        return cariTelefon;
    }

    public void setCariTelefon(String cariTelefon) {
        this.cariTelefon = cariTelefon;
    }

    public String getCariEmail() {
        return cariEmail;
    }

    public void setCariEmail(String cariEmail) {
        this.cariEmail = cariEmail;
    }

    public String getBakiye() {
        return bakiye;
    }

    public void setBakiye(String bakiye) {
        this.bakiye = bakiye;
    }
}