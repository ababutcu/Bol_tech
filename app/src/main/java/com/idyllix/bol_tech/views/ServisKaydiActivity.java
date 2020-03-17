package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.SaveServiceRequest;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.utils.GetCurrentDate;
import com.idyllix.bol_tech.utils.GetIntentData;
import com.idyllix.bol_tech.utils.InternetConnection;
import com.idyllix.bol_tech.viewmodels.CustomerViewModel;
import com.idyllix.bol_tech.viewmodels.ServiceViewModel;

import java.util.ArrayList;
import java.util.List;

public class ServisKaydiActivity extends AppCompatActivity {

    private List<Service> serviceList;
    private Spinner spServisKayitServiceName;
    private Spinner spServisKayitCustomerName;
    private EditText etServisKayitUserName;
    private EditText etServisKayitKonu;
    private EditText etServisKayitTarihi;
    private Button btnServiceKayit;
    private User user;
    private List<String> customerNameList;
    private List<String> serviceNameList;
    private InternetConnection connection;
    private ServiceViewModel serviceViewModel;
    private static String selectedServiceID;
    private EditText etServisKayitCariYetkili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servis_kayitlari);

        init();
        initViews();
        getData();
    }

    private void initViews() {
        spServisKayitCustomerName = findViewById(R.id.spServisKayitCustomerName);
        spServisKayitCustomerName.setOnItemSelectedListener(customerListener);
        spServisKayitServiceName = findViewById(R.id.spServisKayitServiceName);
        spServisKayitServiceName.setOnItemSelectedListener(serviceListener);
        etServisKayitUserName = findViewById(R.id.etServisKayitUserName);
        etServisKayitKonu = findViewById(R.id.etServisKayitKonu);
        etServisKayitTarihi = findViewById(R.id.etServisKayitTarihi);
        etServisKayitCariYetkili = findViewById(R.id.etServisKayitCariYetkili);
        btnServiceKayit = findViewById(R.id.btnYeniServisKayit);
        btnServiceKayit.setOnClickListener(btnListener);
    }

    private void init() {
        serviceList = new ArrayList<>();
        customerNameList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        CustomerViewModel customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        customerViewModel.init();
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel.class);
        serviceViewModel.init();
        connection = new InternetConnection(this);
    }

    private void getData() {
        user = GetIntentData.getUser(this);
        serviceList = GetIntentData.getService(this);
        kayitliServisiGoster(serviceList);
    }

    private void kayitliServisiGoster(List<Service> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!customerNameList.contains(list.get(i).getCariAdi()))
                customerNameList.add(list.get(i).getCariAdi());
        }

        btnServiceKayit.setText(R.string.servis_baslat);
        btnServiceKayit.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ServisKaydiActivity.this,
                android.R.layout.simple_list_item_1, customerNameList);
        spServisKayitCustomerName.setAdapter(adapter);
        setServiceSpinner(list);
    }

    private void setServiceSpinner(List<Service> customerServ) {
        if (customerServ != null) {
            for (int i = 0; i < customerServ.size(); i++) {
                if (spServisKayitCustomerName.getSelectedItem().toString().equals(customerServ.get(i).getCariAdi())) {
                    serviceNameList.clear();
                    serviceNameList.add(customerServ.get(i).getServiceCode());
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<>(ServisKaydiActivity.this,
                            android.R.layout.simple_list_item_1, serviceNameList);
                    spServisKayitServiceName.setAdapter(adapter3);
                }
            }
        }
    }

    private AdapterView.OnItemSelectedListener serviceListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (serviceList != null) {
                        for (int i = 0; i < serviceList.size(); i++) {
                            if (serviceList.get(i).getServiceCode().equals(spServisKayitServiceName.getSelectedItem().toString())) {
                                selectedServiceID = serviceList.get(i).getServiceCode();
                                etServisKayitKonu.setText(serviceList.get(i).getKonu());
                                etServisKayitUserName.setText(String.valueOf(serviceList.get(i).getIstekYapanKullaniciAdi()));
                                etServisKayitTarihi.setText(GetCurrentDate.dateConvertToString(serviceList.get(i).getIstekSaati()));
                                etServisKayitCariYetkili.setText(serviceList.get(i).getIstekYapanCariYetkilisi());
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };

    private View.OnClickListener btnListener = v -> {
        Gson gson = new Gson();
        String userToJson = gson.toJson(user);

        final String time = GetCurrentDate.getThisTime();
        SaveServiceRequest service = new SaveServiceRequest();
        service.setServiceCode(selectedServiceID);
        service.setServisBaslatanKullaniciID(user.getUserID());
        service.setBeginingDate(time);

        if (connection.isNetworkAvailable()) {
            serviceViewModel.startService(null,1, service)
                    .observe(ServisKaydiActivity.this,
                            apiResponse -> {
                                if (apiResponse.getAnswer().equals("1")) {
                                    Toast.makeText(ServisKaydiActivity.this,
                                            R.string.servis_baslatildi, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ServisKaydiActivity.this, OptionsActivity.class);
                                    intent.putExtra("user", userToJson);
                                    startActivity(intent);
                                } else if (apiResponse.getAnswer().equals("0")) {
                                    Toast.makeText(ServisKaydiActivity.this, R.string.servis_baslatilamadi,
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    etServisKayitKonu.setText(apiResponse.getAnswer());
                                }
                            });
        } else {
            openErrorPage();
        }
    };

    private void openErrorPage() {
        Intent intent = new Intent(this, ErrorActivity.class);
        intent.putExtra("activity", 101);
        startActivity(intent);
    }

    private AdapterView.OnItemSelectedListener customerListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setServiceSpinner(serviceList);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
}
