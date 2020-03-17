package com.idyllix.bol_tech.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.repositories.CustomerRepository;

import java.util.List;

public class CustomerViewModel extends ViewModel {

    private CustomerRepository repository;

    public void init(){
        repository= CustomerRepository.getInstance();
    }

    public LiveData<List<Customer>> getCustomers(){
        return repository.getCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers(){
        return repository.getAllCustomers();
    }

}

