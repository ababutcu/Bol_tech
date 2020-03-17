package com.idyllix.bol_tech.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkRequest {

    @Expose
    @SerializedName("ID")
    private int workID;

    @Expose
    @SerializedName("kullaniciID")
    private int userId;

    @Expose
    @SerializedName("iseGiris")
    private String startingDateTime;

    @Expose
    @SerializedName("userName")
    private String userName;

    @Expose
    @SerializedName("istenCikis")
    private String finishingDateTime;

    @Expose
    @SerializedName("yetki")
    private int privilege;


    public WorkRequest() {
    }

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

    public String getStartingDateTime() {
        return startingDateTime;
    }

    public void setStartingDateTime(String startingDateTime) {
        this.startingDateTime = startingDateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFinishingDateTime() {
        return finishingDateTime;
    }

    public void setFinishingDateTime(String finishingDateTime) {
        this.finishingDateTime = finishingDateTime;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
