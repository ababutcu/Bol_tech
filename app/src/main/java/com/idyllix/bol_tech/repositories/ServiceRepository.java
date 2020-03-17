package com.idyllix.bol_tech.repositories;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.idyllix.bol_tech.models.ApiResponse;
import com.idyllix.bol_tech.models.SaveServiceRequest;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.ServiceRequest;
import com.idyllix.bol_tech.models.SifirdanServis;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.retrofit.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ServiceRepository {

    private static ServiceRepository repository;
    private boolean release = true;

    public static ServiceRepository getInstance() {
        if (repository == null) {
            repository = new ServiceRepository();
        }
        return repository;
    }

    public MutableLiveData<List<Service>> getServices(ServiceRequest serviceRequest, int type) {
        final MutableLiveData<List<Service>> data = new MutableLiveData<>();
        Call<List<Service>> call=null;

        if(type==1){
            call=RetrofitClient.getInstance().getApi().getCustomerServices(serviceRequest);
        }else if(type==2){
            call=RetrofitClient.getInstance().getApi().getServices(serviceRequest);
        }

        if (call != null) {
            call.enqueue(new Callback<List<Service>>() {
                @EverythingIsNonNull
                @Override
                public void onResponse(Call<List<Service>> call,
                                       Response<List<Service>> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    if (response.body() != null) {
                        data.setValue(response.body());
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<List<Service>> call, Throwable t) {
                    List<Service> services=new ArrayList<>();
                    Service service=new Service();
                    service.setServiceCode(t.getLocalizedMessage());
                    services.add(service);
                    data.setValue(services);
                }
            });
        }
        return data;
    }

    public MutableLiveData<List<Service>> checkService(User user) {
        final MutableLiveData<List<Service>> data = new MutableLiveData<>();

        Call<List<Service>> call = RetrofitClient.getInstance().getApi().GetServiceNotClosed(user);
        call.enqueue(new Callback<List<Service>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() != null) {
                    data.setValue(response.body());
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Log.d("checkService", "checkService12");
                List<Service> list = new ArrayList<>();
                Service service = new Service(-99, t.getStackTrace()[0].toString());
                list.add(service);
                data.setValue(list);
            }
        });
        return data;
    }

    public MutableLiveData<ApiResponse> closeService(SaveServiceRequest service) {
        final MutableLiveData<ApiResponse> data = new MutableLiveData<>();
        if (!release) {
            ApiResponse response = new ApiResponse("1");
            data.setValue(response);
        } else {
            Call<List<ApiResponse>> call = RetrofitClient.getInstance().getApi().closeService(service);
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
        }
        return data;
    }

    public MutableLiveData<List<Service>> getServiceKayitlari() {
        final MutableLiveData<List<Service>> data = new MutableLiveData<>();

        Call<List<Service>> call = RetrofitClient.getInstance().getApi().getOlusturulmusServisler();
        call.enqueue(new Callback<List<Service>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ServiceLog", "6");
                    return;
                }
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Log.d("Service00", response.body().get(i).getServiceCode());
                    }
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                List<Service> list = new ArrayList<>();
                Service service = new Service(-99, "Hata", -99, t.getLocalizedMessage(),
                        t.getMessage(), t.getStackTrace()[0].toString(), -99, "Hata");
                list.add(service);
                data.setValue(list);
            }
        });
        return data;
    }

    public MutableLiveData<ApiResponse> startService(SifirdanServis servis, int durum, SaveServiceRequest request) {
        final MutableLiveData<ApiResponse> data = new MutableLiveData<>();
        if (!release) {
            ApiResponse response = new ApiResponse("1");
            data.setValue(response);
        } else {
            Call<List<ApiResponse>> call;
            if (durum == 1) {
                //isteği bulunan servisi başlat
                call = RetrofitClient.getInstance().getApi().updateService(request);
            } else {
                //sıfırdan servis başlat
                call = RetrofitClient.getInstance().getApi().startService(servis);
            }
            if (call != null) {
                call.enqueue(new Callback<List<ApiResponse>>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<List<ApiResponse>> call, Response<List<ApiResponse>> response) {
                        if (!response.isSuccessful()) {
                            Log.d("ServiceLog", "0");
                            return;
                        }
                        if (response.body() != null) {
                            Log.d("ServiceLog", "1");
                            data.setValue(response.body().get(0));
                        }
                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<List<ApiResponse>> call, Throwable t) {
                        Log.d("ServiceLog", "2");
                        data.setValue(new ApiResponse("error : " + t.getMessage() + " -" + t.getLocalizedMessage()));
                    }
                });
            }
        }
        return data;
    }

    public MutableLiveData<ApiResponse> saveServiceRequest(SaveServiceRequest service) {
        final MutableLiveData<ApiResponse> data = new MutableLiveData<>();
        if (!release) {
            ApiResponse response = new ApiResponse("1");
            data.setValue(response);
        } else {
            Call<List<ApiResponse>> call = RetrofitClient.getInstance().getApi().saveServiceRequest(service);
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
                    data.setValue(new ApiResponse("error : " + t.getLocalizedMessage()));
                }
            });
        }
        return data;
    }
}
