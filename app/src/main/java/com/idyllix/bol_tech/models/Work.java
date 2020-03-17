package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Work {

    @Expose
    @SerializedName("ID")
    private int workID;

    @Expose
    @SerializedName("kullaniciID")
    private int userId;

    @Expose
    @SerializedName("iseGiris")
    private Date startingDateTime;

    @Expose
    @SerializedName("adiSoyadi")
    private String userName;

    @Expose
    @SerializedName("istenCikis")
    private Date finishingDateTime;

    public Work(String userName) {
        this.userName = userName;
    }

    public Work() {}

    public int getWorkID() {
        return workID;
    }

    public void setWorkID(int workID) {
        this.workID = workID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getStartingDateTime() {
        return startingDateTime;
    }

    public void setStartingDateTime(Date startingDateTime) {
        this.startingDateTime = startingDateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getFinishingDateTime() {
        return finishingDateTime;
    }

    public void setFinishingDateTime(Date finishingDateTime) {
        this.finishingDateTime = finishingDateTime;
    }
}
