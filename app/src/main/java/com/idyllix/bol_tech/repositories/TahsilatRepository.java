package com.idyllix.bol_tech.repositories;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.idyllix.bol_tech.models.ApiResponse;
import com.idyllix.bol_tech.models.Collecting;
import com.idyllix.bol_tech.models.Collecting2;
import com.idyllix.bol_tech.models.CollectingQuery;
import com.idyllix.bol_tech.retrofit.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class TahsilatRepository {

    private static TahsilatRepository repository;

    public static TahsilatRepository getInstance() {
        if (repository == null) {
            repository = new TahsilatRepository();
        }
        return repository;
    }

    public MutableLiveData<ApiResponse> tahsilatYap(Collecting collecting) {
        final MutableLiveData<ApiResponse> data = new MutableLiveData<>();
            Call<List<ApiResponse>> call = RetrofitClient.getInstance().getApi().tahsilatyap(collecting);
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
                    Log.d("ServisRepo2","error : " + t.getLocalizedMessage());
                    data.setValue(new ApiResponse("error : " + t.getLocalizedMessage()));
                }
            });
        return data;
    }

    public MutableLiveData<List<Collecting2>> getAllCollecting(CollectingQuery query, int methodNumber) {
        final MutableLiveData<List<Collecting2>> data = new MutableLiveData<>();
        Call<List<Collecting2>> call=null;
        if(methodNumber==1){
            call=RetrofitClient.getInstance().getApi().getUserClientCollecting(query);
            Log.d("TahsilatRapor","repo-1");
        }else if (methodNumber==2){
            call=RetrofitClient.getInstance().getApi().getClientCollecting(query);
            Log.d("TahsilatRapor","repo-2");
        }else if(methodNumber==3){
            call=RetrofitClient.getInstance().getApi().getUserCollecting(query);
            Log.d("TahsilatRapor","repo-3");
        }else if (methodNumber==4){
            call=RetrofitClient.getInstance().getApi().getAllCollecting(query);
            Log.d("TahsilatRapor","repo-4");
        }

        if (call != null) {
            call.enqueue(new Callback<List<Collecting2>>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<List<Collecting2>> call, Response<List<Collecting2>> response) {
                    if (!response.isSuccessful()) {
                        Log.d("TahsilatRapor",""+response.code());
                        return;
                    }
                    if (response.body() != null) {
                        data.setValue(response.body());
                        Log.d("TahsilatRapor","response-2");
                    }
                }
                @EverythingIsNonNull
                @Override
                public void onFailure(Call<List<Collecting2>> call, Throwable t) {
                        List<Collecting2> collectings=new ArrayList<>();
                        Collecting2 collecting=new Collecting2();
                        collecting.setAciklama(t.getLocalizedMessage());
                        collectings.add(collecting);
                        data.setValue(collectings);
                    Log.d("TahsilatRapor","response-3");
                }
            });
        }
        return  data;
    }
}
