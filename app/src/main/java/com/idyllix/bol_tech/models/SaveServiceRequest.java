package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveServiceRequest {

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

    //////////////////////////////////////////////////////

    @Expose
    @SerializedName("yapilanIslemler")
    private String description;

    @Expose
    @SerializedName("imzaAtanKisi")
    private String imzaAtanKisi;

    @Expose
    @SerializedName("yetkiliImzasi")
    private String yetkiliImzasi;

    @Expose
    @SerializedName("kullanilanMalzemeler")
    private String usage;

    @Expose
    @SerializedName("servisYapanKullaniciID")
    private int servisYapanID;

    @Expose
    @SerializedName("servisBitisTarihi")
    private String finishingDate;

    @Expose
    @SerializedName("items")
    private List<Item> items;

    public SaveServiceRequest() {}

    public SaveServiceRequest(String serviceCode,
                              String description,
                              String usage,
                              String finishingDate,
                              String imzaAtanKisi,
                              String yetkiliImzasi,
                               int servisYapanID) {
        this.serviceCode = serviceCode;
        this.description = description;
        this.imzaAtanKisi = imzaAtanKisi;
        this.yetkiliImzasi = yetkiliImzasi;
        this.usage = usage;
        this.servisYapanID = servisYapanID;
        this.finishingDate = finishingDate;
    }

    public SaveServiceRequest(String serviceCode, int customerID,
                              String istekYapanCariYetkilisi, int istekYapanKullaniciID,
                              String konu, String istekSaati) {
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
        this.istekYapanKullaniciID = istekYapanKullaniciID;
        this.konu = konu;
        this.istekSaati = istekSaati;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImzaAtanKisi() {
        return imzaAtanKisi;
    }

    public void setImzaAtanKisi(String imzaAtanKisi) {
        this.imzaAtanKisi = imzaAtanKisi;
    }

    public String getYetkiliImzasi() {
        return yetkiliImzasi;
    }

    public void setYetkiliImzasi(String yetkiliImzasi) {
        this.yetkiliImzasi = yetkiliImzasi;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public int getServisYapanID() {
        return servisYapanID;
    }

    public void setServisYapanID(int servisYapanID) {
        this.servisYapanID = servisYapanID;
    }

    public String getFinishingDate() {
        return finishingDate;
    }

    public void setFinishingDate(String finishingDate) {
        this.finishingDate = finishingDate;
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
