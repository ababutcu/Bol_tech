package com.idyllix.bol_tech.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class UserRepository {
    private static UserRepository repository;

    public static UserRepository getInstance() {
        if (repository == null) {
            repository = new UserRepository();
        }
        return repository;
    }

    public LiveData<User> checkUser(User user) {
        final MutableLiveData<User> data = new MutableLiveData<>();
            Call<List<User>> call = RetrofitClient.getInstance().getApi().checkUser(user);
            call.enqueue(new Callback<List<User>>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    if (response.body() != null) {
                        data.setValue(response.body().get(0));
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    data.setValue(new User("hata :", t.getLocalizedMessage()));
                }
            });
        return data;
    }

    public LiveData<List<User>> getAllUsers() {
        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        Call<List<User>> call = RetrofitClient.getInstance().getApi().getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
