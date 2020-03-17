package com.idyllix.bol_tech.repositories;

import androidx.lifecycle.MutableLiveData;

import com.idyllix.bol_tech.models.Item;
import com.idyllix.bol_tech.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ItemRepository {

    private static ItemRepository repository;
    public  static ItemRepository getInstance(){
        if(repository==null){
            repository=new ItemRepository();
        }
        return repository;
    }

    public MutableLiveData<List<Item>> getAllItems(){

        final MutableLiveData<List<Item>> data=new MutableLiveData<>();

        Call<List<Item>> call= RetrofitClient.getInstance().getApi().getAllItems();

        call.enqueue(new Callback<List<Item>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return  data;
    }
}
