package com.idyllix.bol_tech.repositories;

import androidx.lifecycle.MutableLiveData;

import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class CustomerRepository {

    private static CustomerRepository repository;
    private boolean release=true;

    public  static CustomerRepository getInstance(){
        if(repository==null){
            repository=new CustomerRepository();
        }
        return repository;
    }

    public MutableLiveData<List<Customer>> getCustomers(){
        final MutableLiveData<List<Customer>> data=new MutableLiveData<>();
        if (!release){
            List<Customer> customerList=new ArrayList<>();
            /*customerList.add(new Customer(1,"Müşteri 1","0123456789","-1205"));
            customerList.add(new Customer(2,"Müşteri 2","1234567890","300"));
            customerList.add(new Customer(3,"Müşteri 3","2345678901","150"));
            customerList.add(new Customer(4,"Müşteri 4","3456789012","100"));
            customerList.add(new Customer(5,"Müşteri 5","0242662896","1000"));*/
            data.setValue(customerList);
        }else{
            Call<List<Customer>> call= RetrofitClient.getInstance().getApi().getCustomers();
            call.enqueue(new Callback<List<Customer>>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                    if(!response.isSuccessful()){
                        return;
                    }
                    if (response.body() != null) {
                        data.setValue(response.body());
                    }
                }
                @EverythingIsNonNull
                @Override
                public void onFailure(Call<List<Customer>> call, Throwable t) {
                    data.setValue(null);
                }
            });
        }
        return data;
    }

    public MutableLiveData<List<Customer>> getAllCustomers() {
        final MutableLiveData<List<Customer>> data=new MutableLiveData<>();
        Call<List<Customer>> call= RetrofitClient.getInstance().getApi().getAllCustomers();
        call.enqueue(new Callback<List<Customer>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
