package com.idyllix.bol_tech.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.idyllix.bol_tech.models.ApiResponse;
import com.idyllix.bol_tech.models.SaveServiceRequest;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.ServiceRequest;
import com.idyllix.bol_tech.models.SifirdanServis;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.repositories.ServiceRepository;

import java.util.List;

public class ServiceViewModel extends ViewModel {
    private ServiceRepository repository;

    public void init(){
        repository= ServiceRepository.getInstance();
    }

    public LiveData<List<Service>> getServices(ServiceRequest serviceRequest, int type){
        Log.d("ServiceList","serviceViewmodel");
        return repository.getServices(serviceRequest,type);
    }

    public LiveData<ApiResponse> startService(SifirdanServis servis,int durum, SaveServiceRequest request){
        return repository.startService(servis,durum,request);
    }

    public LiveData<List<Service>> checkService(User user){
        return repository.checkService(user);
    }

    public LiveData<ApiResponse> closeService(SaveServiceRequest service){
        return repository.closeService(service);
    }

    public LiveData<List<Service>> getServiceKayitlari(){
        return repository.getServiceKayitlari();
    }

    public LiveData<ApiResponse> saveServiceRequest(SaveServiceRequest service){
        return repository.saveServiceRequest(service);
    }

    public LiveData<List<Service>> getCustomerService(ServiceRequest request, int type) {
        return repository.getServices(request,type);
    }


}

