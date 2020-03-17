package com.idyllix.bol_tech.retrofit;

import com.idyllix.bol_tech.models.ApiResponse;
import com.idyllix.bol_tech.models.Collecting2;
import com.idyllix.bol_tech.models.CollectingQuery;
import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.models.Item;
import com.idyllix.bol_tech.models.SaveServiceRequest;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.ServiceRequest;
import com.idyllix.bol_tech.models.SifirdanServis;
import com.idyllix.bol_tech.models.Collecting;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.models.Work;
import com.idyllix.bol_tech.models.WorkQuery;
import com.idyllix.bol_tech.models.WorkRequest;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    //User
    @POST("api/other/checkUser")
    Call<List<User>> checkUser(@Body User user);

    @POST("api/kullanici/getAllData")
    Call<List<User>> getAllUsers();

    //Customer
    @POST("api/other/GetCustomerMin")
    Call<List<Customer>> getCustomers();

    //Customer
    @POST("api/other/GetAllCustomers")
    Call<List<Customer>> getAllCustomers();

    //Service
    @POST("api/other/GetServiceNotClosed")
    Call<List<Service>> GetServiceNotClosed(@Body User user);

    @POST("api/other/GetServices")
    Call<List<Service>> getServices(@Body ServiceRequest serviceRequest);

    @POST("api/other/getCustomerServices")
    Call<List<Service>> getCustomerServices(@Body ServiceRequest serviceRequest);

    @POST("api/other/closeService")
    Call<List<ApiResponse>> closeService(@Body SaveServiceRequest service);

    @POST("api/other/GetServiceRequests")
    Call<List<Service>> getOlusturulmusServisler();

    @POST("api/other/saveServiceWithOutRequest")
    Call<List<ApiResponse>> startService(@Body SifirdanServis service);

    @POST("api/other/startSavedService")
    Call<List<ApiResponse>> updateService(@Body SaveServiceRequest service);

    @POST("api/other/saveServiceRequest")
    Call<List<ApiResponse>> saveServiceRequest(@Body SaveServiceRequest service);

    //Work
    @POST("api/other/GetWorkWithUserID")
    Call<List<Work>> checkWorking(@Body Work work);

    @POST("api/other/startWork")
    Call<List<ApiResponse>> startWorking(@Body WorkRequest work);

    @POST("api/other/finishWork")
    Call<List<ApiResponse>> finishWorking(@Body WorkRequest work);

    @POST("api/other/GetAllWork")
    Call<List<Work>> getAllWorkings(@Body WorkQuery request);

    @POST("api/other/GetUserWork")
    Call<List<Work>> getUserWorkingReport(@Body WorkQuery request);

    //Collecting
    @POST("api/other/Collect")
    Call<List<ApiResponse>> tahsilatyap(@Body Collecting collecting);

    @POST("api/other/GetUserCollecting")
    Call<List<Collecting2>> getUserCollecting(@Body CollectingQuery request);

    @POST("api/other/GetClientCollecting")
    Call<List<Collecting2>> getClientCollecting(@Body CollectingQuery request);

    @POST("api/other/GetUserClientCollecting")
    Call<List<Collecting2>> getUserClientCollecting(@Body CollectingQuery request);

    @POST("api/other/GetAllCollecting")
    Call<List<Collecting2>> getAllCollecting(@Body CollectingQuery request);

    //Item
    @POST("api/other/GetAllItems")
    Call<List<Item>> getAllItems();
}