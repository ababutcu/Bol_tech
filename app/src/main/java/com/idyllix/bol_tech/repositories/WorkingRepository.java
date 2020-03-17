package com.idyllix.bol_tech.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.idyllix.bol_tech.models.ApiResponse;
import com.idyllix.bol_tech.models.Work;
import com.idyllix.bol_tech.models.WorkQuery;
import com.idyllix.bol_tech.models.WorkRequest;
import com.idyllix.bol_tech.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class WorkingRepository {
    private static WorkingRepository repository;

    public static WorkingRepository getInstance() {
        if (repository == null) {
            repository = new WorkingRepository();
        }
        return repository;
    }

    public LiveData<ApiResponse> startWorking(WorkRequest work) {
        final MutableLiveData<ApiResponse> data = new MutableLiveData<>();

        Call<List<ApiResponse>> call = RetrofitClient.getInstance().getApi().startWorking(work);
        call.enqueue(new Callback<List<ApiResponse>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body().get(0));
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                data.setValue(new ApiResponse(t.getLocalizedMessage()));
            }
        });
        return data;
    }

    public MutableLiveData<Work> checkWorking(Work work) {
        final MutableLiveData<Work> data = new MutableLiveData<>();
        Call<List<Work>> call = RetrofitClient.getInstance().getApi().checkWorking(work);
        call.enqueue(new Callback<List<Work>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Work>> call, Response<List<Work>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() != null && response.body().size() > 0) {
                    data.setValue(response.body().get(0));
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<Work>> call, Throwable t) {
                data.setValue(new Work(t.getLocalizedMessage()));
            }
        });
        return data;
    }

    public LiveData<ApiResponse> finishWorking(WorkRequest work) {
        final MutableLiveData<ApiResponse> data = new MutableLiveData<>();
        Call<List<ApiResponse>> call = RetrofitClient.getInstance().getApi().finishWorking(work);
        call.enqueue(new Callback<List<ApiResponse>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body().get(0));
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                data.setValue(new ApiResponse(t.getLocalizedMessage()));
            }
        });
        return data;
    }

    public LiveData<List<Work>> getAllWorkings(WorkQuery request, int type) {
        final MutableLiveData<List<Work>> data = new MutableLiveData<>();
        Call<List<Work>> call = null;
        if (type == 0) {
            Log.d("GirisCikis2","repo0");
            call = RetrofitClient.getInstance().getApi().getAllWorkings(request);
        } else if (type == 1) {
            Log.d("GirisCikis2","repo10---"+request.getUserID());
            Log.d("GirisCikis2","repo11---"+request.getYetki());
            Log.d("GirisCikis2","repo12---"+request.getBaslangic());
            Log.d("GirisCikis2","repo13---"+request.getBitis());
            call = RetrofitClient.getInstance().getApi().getUserWorkingReport(request);
        }
        if (call != null) {
            Log.d("GirisCikis2","repo2");
            call.enqueue(new Callback<List<Work>>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<List<Work>> call, Response<List<Work>> response) {
                    Log.d("GirisCikis2","repo3");
                    if (!response.isSuccessful()) {
                        Log.d("GirisCikis2","repo4");
                        return;
                    }

                    if (response.body() != null) {
                        Log.d("GirisCikis2","repo5");
                        data.setValue(response.body());
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<List<Work>> call, Throwable t) {
                    Log.d("GirisCikis2","repo6");
                    List<Work> works=new ArrayList<>();
                    Work work=new Work();
                    work.setUserName(t.getLocalizedMessage());
                    works.add(work);
                    data.setValue(works);
                }
            });
        }
        return data;
    }
}
