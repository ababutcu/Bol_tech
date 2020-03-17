package com.idyllix.bol_tech.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.models.SaveServiceRequest;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.utils.GetCurrentDate;
import com.idyllix.bol_tech.utils.GetIntentData;
import com.idyllix.bol_tech.utils.HideKeyboard;
import com.idyllix.bol_tech.utils.InternetConnection;
import com.idyllix.bol_tech.utils.Randoms;
import com.idyllix.bol_tech.viewmodels.CustomerViewModel;
import com.idyllix.bol_tech.viewmodels.ServiceViewModel;

import java.util.ArrayList;
import java.util.List;

public class YeniServisKaydiActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView; 
    private EditText etYeniServisKayitMusteri;
    private EditText etYeniServisKayitCariYetkili;
    private EditText etYeniServisKayitKonu;
    private List<Customer> allCustomers;
    private ArrayAdapter<String> autoCompleteAdapter;

    private CustomerViewModel customerViewModel;
    private ServiceViewModel serviceViewModel;
    private User user;
    private List<String> allCustomerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_servis_kaydi);

        init();
    }

    private void init() {
        autoCompleteTextView = findViewById(R.id.autoYeniKayitCustomer);
        autoCompleteTextView.setOnItemClickListener(autoCompleteListener);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        etYeniServisKayitMusteri = findViewById(R.id.etYeniServisKayitMusteri);
        etYeniServisKayitCariYetkili = findViewById(R.id.etYeniServisKayitCariYetkili);
        etYeniServisKayitKonu = findViewById(R.id.etYeniServisKayitKonu);

        Button btnYeniServisKayit = findViewById(R.id.btnYeniServisKayit);
        btnYeniServisKayit.setOnClickListener(btnListener);

        allCustomers = new ArrayList<>();
        allCustomerNames=new ArrayList<>();
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel.class);
        customerViewModel.init();
        serviceViewModel.init();
        user = GetIntentData.getUser(this);
        getCustomers();
    }

    private void getCustomers() {
        customerViewModel.getCustomers().observe(this, customers -> {
            allCustomers.addAll(customers);
            allCustomerNames = new ArrayList<>();
            for (int i = 0; i < customers.size(); i++) {
                allCustomerNames.add(allCustomers.get(i).getCustomerName());
            }

            autoCompleteAdapter = new ArrayAdapter<>(getApplicationContext(),
                    R.layout.item_autocomplete,
                    allCustomerNames);

            autoCompleteTextView.setAdapter(autoCompleteAdapter);
        });
    }

    private TextWatcher textWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (start == 0 && before == 1 && count == 0) {
                allCustomers.clear();
                allCustomerNames.clear();
                HideKeyboard.hide(YeniServisKaydiActivity.this);
                getCustomers();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private AdapterView.OnItemClickListener autoCompleteListener =
            (parent, view, position, id) -> {
                etYeniServisKayitMusteri.setText("");
                etYeniServisKayitMusteri.setText(autoCompleteTextView.getText().toString());
            };

    private View.OnClickListener btnListener = v -> servisKaydet();

    private void servisKaydet() {
        InternetConnection connection = new InternetConnection(this);
        Gson gson = new Gson();
        String userToJson = gson.toJson(user);
        if (connection.isNetworkAvailable()) {
            String date = GetCurrentDate.getThisTime();
            int cariID = -1;
            for (int i = 0; i < allCustomers.size(); i++) {
                if (etYeniServisKayitMusteri.getText().toString().trim().equals(allCustomers.get(i).getCustomerName())) {
                    cariID = allCustomers.get(i).getId();
                }
            }
            String serviceCode = Randoms.createRandomString();
            String cariYetkilisi = etYeniServisKayitCariYetkili.getText().toString().trim();
            int kullaniciID = user.getUserID();
            String konu = etYeniServisKayitKonu.getText().toString().trim();

            SaveServiceRequest request =
                    new SaveServiceRequest(serviceCode, cariID, cariYetkilisi, kullaniciID, konu, date);

            serviceViewModel.saveServiceRequest(request).observe(this, apiResponse -> {
                if (apiResponse.getAnswer().equals("1")) {
                    Toast.makeText(YeniServisKaydiActivity.this,
                            R.string.servis_istegi_baslatildi, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(YeniServisKaydiActivity.this, OptionsActivity.class);
                    intent.putExtra("user", userToJson);
                    startActivity(intent);
                } else {
                    Toast.makeText(YeniServisKaydiActivity.this, R.string.servis_istegi_baslatilamadi,
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Intent intent = new Intent(this, ErrorActivity.class);
            intent.putExtra("activity", 301);
            startActivity(intent);
        }
    }
}
