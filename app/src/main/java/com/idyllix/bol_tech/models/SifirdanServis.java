package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SifirdanServis {

    @Expose
    @SerializedName("servisCode")
    private String serviceCode;

    @Expose
    @SerializedName("cariID")
    private int customerID;

    @Expose
    @SerializedName("istekYapanCariYetkilisi")
    private String istekYapanCariYetkilisi;

    @Expose
    @SerializedName("istekKaydedenKullaniciID")
    private int istekYapanKullaniciID;

    @Expose
    @SerializedName("istekKonusu")
    private String konu;

    @Expose
    @SerializedName("istekTarihi")
    private String istekSaati;

    @Expose
    @SerializedName("servisBaslatanKullaniciID")
    private int servisBaslatanKullaniciID;

    @Expose
    @SerializedName("servisBaslangicTarihi")
    private String beginingDate;

    public SifirdanServis() {
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getIstekYapanCariYetkilisi() {
        return istekYapanCariYetkilisi;
    }

    public void setIstekYapanCariYetkilisi(String istekYapanCariYetkilisi) {
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
    }

    public int getIstekYapanKullaniciID() {
        return istekYapanKullaniciID;
    }

    public void setIstekYapanKullaniciID(int istekYapanKullaniciID) {
        this.istekYapanKullaniciID = istekYapanKullaniciID;
    }

    public String getKonu() {
        return konu;
    }

    public void setKonu(String konu) {
        this.konu = konu;
    }

    public String getIstekSaati() {
        return istekSaati;
    }

    public void setIstekSaati(String istekSaati) {
        this.istekSaati = istekSaati;
    }

    public int getServisBaslatanKullaniciID() {
        return servisBaslatanKullaniciID;
    }

    public void setServisBaslatanKullaniciID(int servisBaslatanKullaniciID) {
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
    }

    public String getBeginingDate() {
        return beginingDate;
    }

    public void setBeginingDate(String beginingDate) {
        this.beginingDate = beginingDate;
    }
}
