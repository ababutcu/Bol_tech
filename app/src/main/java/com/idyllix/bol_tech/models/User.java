package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("ID")
    private int userID;

    @Expose
    @SerializedName("kullaniciAdi")
    private String userName;

    @Expose
    @SerializedName("sifre")
    private String password;

    @Expose
    @SerializedName("yetkiID")
    private int privilege;

    @Expose
    @SerializedName("telefon")
    private  String telefon;

    @Expose
    @SerializedName("email")
    private String email;

    public User(int userID, String userName, String password, int privilege, String telefon, String email) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.privilege = privilege;
        this.telefon = telefon;
        this.email = email;
    }

    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password, int privilege) {
        this.userName = userName;
        this.password = password;
        this.privilege = privilege;
    }

    public User(int userID, String userName, String password, int privilege) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.privilege = privilege;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
