package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Service {

    @Expose
    @SerializedName("ID")
    private int id;

    @Expose
    @SerializedName("servisCode")
    private String serviceCode;

    @Expose
    @SerializedName("cariID")
    private int customerID;

    @Expose
    @SerializedName("adi")
    private String cariAdi;

    @Expose
    @SerializedName("unvani")
    private String unvani;

    @Expose
    @SerializedName("istekKonusu")
    private String konu;

    @Expose
    @SerializedName("istekYapanCariYetkilisi")
    private String istekYapanCariYetkilisi;

    @Expose
    @SerializedName("istekKaydedenKullaniciID")
    private int istekYapanKullaniciID;

    @Expose
    @SerializedName("istekKaydedenKullaniciAdi")
    private String istekYapanKullaniciAdi;

    @Expose
    @SerializedName("istekTarihi")
    private Date istekSaati;

    @Expose
    @SerializedName("servisBaslatanKullaniciID")
    private int servisBaslatanKullaniciID;

    @Expose
    @SerializedName("servisBaslangicTarihi")
    private Date beginingDate;

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
    private Date finishingDate;

    @Expose
    @SerializedName("adiSoyadi")
    private String adiSoyadi;

    public Service(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Service(int id, String serviceCode) {
        this.id = id;
        this.serviceCode = serviceCode;
    }

    public Service(int id, String serviceCode, int customerID, String cariAdi,
                   String konu, String istekYapanCariYetkilisi, int istekYapanKullaniciID,
                   String istekYapanKullaniciAdi) {
        this.id = id;
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.cariAdi = cariAdi;
        this.konu = konu;
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
        this.istekYapanKullaniciID = istekYapanKullaniciID;
        this.istekYapanKullaniciAdi = istekYapanKullaniciAdi;
    }

    public Service(String serviceCode, int customerID, int servisBaslatanKullaniciID, Date beginingDate) {
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
        this.beginingDate = beginingDate;
    }

    public Service(int id, int customerID, int servisBaslatanKullaniciID, Date beginingDate) {
        this.id = id;
        this.customerID = customerID;
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
        this.beginingDate = beginingDate;
    }

    public Service(String serviceCode, int customerID, String konu, String istekYapanCariYetkilisi,
                   int istekYapanKullaniciID, Date istekSaati, int servisBaslatanKullaniciID, Date beginingDate,
                   String description, String imzaAtanKisi, String yetkiliImzasi, String usage, int servisYapanID, Date finishingDate) {
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.konu = konu;
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
        this.istekYapanKullaniciID = istekYapanKullaniciID;
        this.istekSaati = istekSaati;
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
        this.beginingDate = beginingDate;
        this.description = description;
        this.imzaAtanKisi = imzaAtanKisi;
        this.yetkiliImzasi = yetkiliImzasi;
        this.usage = usage;
        this.servisYapanID = servisYapanID;
        this.finishingDate = finishingDate;
    }

    public Service(int id, String serviceCode, int customerID, String konu, String istekYapanCariYetkilisi,
                   int istekYapanKullaniciID, Date istekSaati, int servisBaslatanKullaniciID, Date beginingDate,
                   String description, String imzaAtanKisi, String yetkiliImzasi, String usage, int servisYapanID, Date finishingDate) {
        this.id = id;
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.konu = konu;
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
        this.istekYapanKullaniciID = istekYapanKullaniciID;
        this.istekSaati = istekSaati;
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
        this.beginingDate = beginingDate;
        this.description = description;
        this.imzaAtanKisi = imzaAtanKisi;
        this.yetkiliImzasi = yetkiliImzasi;
        this.usage = usage;
        this.servisYapanID = servisYapanID;
        this.finishingDate = finishingDate;
    }

    public Service(int id, String serviceCode, int customerID, String konu, String istekYapanCariYetkilisi,
                   int istekYapanKullaniciID,
                   Date istekSaati, int servisBaslatanKullaniciID, Date beginingDate) {
        this.id = id;
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.konu = konu;
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
        this.istekYapanKullaniciID = istekYapanKullaniciID;
        this.istekSaati = istekSaati;
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
        this.beginingDate = beginingDate;
    }

    public Service(int id, String serviceCode, int customerID, String konu,
                   String istekYapanCariYetkilisi, int istekYapanKullaniciID, Date istekSaati) {
        this.id = id;
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.konu = konu;
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
        this.istekYapanKullaniciID = istekYapanKullaniciID;
        this.istekSaati = istekSaati;
    }

    public Service(String serviceCode, int customerID,
                   String istekYapanCariYetkilisi, int istekYapanKullaniciID, String konu, Date istekSaati,
                   int servisBaslatanKullaniciID, Date beginingDate) {
        this.serviceCode = serviceCode;
        this.customerID = customerID;
        this.konu = konu;
        this.istekYapanCariYetkilisi = istekYapanCariYetkilisi;
        this.istekYapanKullaniciID = istekYapanKullaniciID;
        this.istekSaati = istekSaati;
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
        this.beginingDate = beginingDate;
    }

    public Service(String serviceCode, int servisBaslatanKullaniciID, Date beginingDate) {
        this.serviceCode = serviceCode;
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
        this.beginingDate = beginingDate;
    }

    public Service() {}

    public String getIstekYapanKullaniciAdi() {
        return istekYapanKullaniciAdi;
    }

    public void setIstekYapanKullaniciAdi(String istekYapanKullaniciAdi) {
        this.istekYapanKullaniciAdi = istekYapanKullaniciAdi;
    }

    public String getUnvani() {
        return unvani;
    }

    public void setUnvani(String unvani) {
        this.unvani = unvani;
    }

    public String getAdiSoyadi() {
        return adiSoyadi;
    }

    public void setAdiSoyadi(String adiSoyadi) {
        this.adiSoyadi = adiSoyadi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getKonu() {
        return konu;
    }

    public void setKonu(String konu) {
        this.konu = konu;
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

    public Date getIstekSaati() {
        return istekSaati;
    }

    public void setIstekSaati(Date istekSaati) {
        this.istekSaati = istekSaati;
    }

    public Date getFinishingDate() {
        return finishingDate;
    }

    public void setFinishingDate(Date finishingDate) {
        this.finishingDate = finishingDate;
    }

    public int getServisBaslatanKullaniciID() {
        return servisBaslatanKullaniciID;
    }

    public void setServisBaslatanKullaniciID(int servisBaslatanKullaniciID) {
        this.servisBaslatanKullaniciID = servisBaslatanKullaniciID;
    }

    public Date getBeginingDate() {
        return beginingDate;
    }

    public void setBeginingDate(Date beginingDate) {
        this.beginingDate = beginingDate;
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

    public String getCariAdi() {
        return cariAdi;
    }

    public void setCariAdi(String cariAdi) {
        this.cariAdi = cariAdi;
    }
}
