package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.SifirdanServis;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.models.Work;
import com.idyllix.bol_tech.models.WorkRequest;
import com.idyllix.bol_tech.utils.GetCurrentDate;
import com.idyllix.bol_tech.utils.HideKeyboard;
import com.idyllix.bol_tech.utils.InternetConnection;
import com.idyllix.bol_tech.utils.Randoms;
import com.idyllix.bol_tech.viewmodels.CustomerViewModel;
import com.idyllix.bol_tech.viewmodels.ServiceViewModel;
import com.idyllix.bol_tech.viewmodels.WorkingViewModel;
import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends AppCompatActivity {

    private View viewMain;
    private View viewService;
    private View viewWorking;
    private Button btnServisKapat;
    private Button btnServisAc;
    private Button btnGirisCikis;
    private Button btnMain;
    private Button btnIstekKaydiOlustur;
    private Button btnIstekKayitlariniGetir;
    private User user;
    private TextView tvDateTime;
    private AlertDialog mainAlertDialog;
    private ServiceViewModel serviceViewModel;
    private InternetConnection connection;
    private List<Service> kullanicininActigiServisler;
    private List<Customer> allCustomers;
    private CustomerViewModel customerViewModel;
    private WorkingViewModel workingViewModel;
    private TextView tvWorkingStartingTime;
    private List<Service> olusturulmusServislerListesi;
    private EditText etServisKonusu;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> autoCompleteAdapter;
    private EditText etCustomerName;
    private EditText etCariYetkilisi;
    private static int workID = -1;
    private AlertDialog servicesAlertDialog;
    private List<String>allCustomerNames;
    private AlertDialog workingAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        init();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connection.isNetworkAvailable()) {
            //kullanıcının başlattığı bir servis olup olmadığı sorgulanır.
            checkService(user);

            //İlk gösterilecek alert dialog
            showMainAlertDialog();
        }
    }

    /* İşe giriş-çıkış işlemlerini yapan alert dialog*/
    private void showWorkingAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        viewWorking = View.inflate(this, R.layout.alert_dialog_working, null);
        builder.setView(viewWorking);
        builder.setCancelable(false);
        workingAlertDialog = builder.create();
        workingAlertDialog.show();
        initWorkingAlertDialog();
    }

    private void initWorkingAlertDialog() {

        Button btnWorking = viewWorking.findViewById(R.id.btnWorking);
        btnWorking.setOnClickListener(startWorkingListener);
        tvWorkingStartingTime = viewWorking.findViewById(R.id.tvWorkingStartingTime);
        tvWorkingStartingTime.setVisibility(View.GONE);
        Button btnCloseWorkAlert=viewWorking.findViewById(R.id.btnCloseWorkAlert);
        btnCloseWorkAlert.setOnClickListener(v ->{
            workingAlertDialog.dismiss();
            closeApp();
        });

        if (workID != -1) {
            btnWorking.setText(getString(R.string.isten_cikis));
        }
    }

    private void checkWorking(User user1) {
        Work work1 = new Work();
        work1.setUserId(user1.getUserID());
        workingViewModel.checkWorking(work1)
                .observe(this, work ->{
                    if(work!=null){
                        workID = work.getWorkID();
                    }
                });
    }

    private View.OnClickListener startWorkingListener = v -> {
        if (workID == -1) {
            //İşe Giriş
            String swt = GetCurrentDate.getThisTime();
            WorkRequest work = new WorkRequest();
            work.setUserId(user.getUserID());
            work.setStartingDateTime(swt);
            if (connection.isNetworkAvailable()) {
                workingViewModel.startWorking(work).observe(this, apiResponse -> {
                    if (apiResponse.getAnswer().equals("1")) {
                        tvWorkingStartingTime.setText(swt);
                        tvWorkingStartingTime.setVisibility(View.VISIBLE);
                        closeApp();
                        checkService(user);
                    } else {
                        Toast.makeText(OptionsActivity.this, R.string.ise_giris_basarisiz,
                                Toast.LENGTH_LONG).show();
                        tvWorkingStartingTime.setText(apiResponse.getAnswer());
                        tvWorkingStartingTime.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                openErrorPage(22);
            }
        } else {
            //İşten Çıkış
            if (connection.isNetworkAvailable()) {
                String fwt = GetCurrentDate.getThisTime();
                WorkRequest work3 = new WorkRequest();
                work3.setWorkID(workID);
                work3.setFinishingDateTime(fwt);
                workingViewModel.finishWorking(work3).observe(this, apiResponse -> {
                    if (apiResponse.getAnswer().equals("1")) {
                        Toast.makeText(OptionsActivity.this, R.string.isten_cilis_basarili,
                                Toast.LENGTH_LONG).show();
                        showMainAlertDialog();
                        checkService(user);
                    } else {
                        Toast.makeText(OptionsActivity.this, R.string.isten_cilis_basarisiz,
                                Toast.LENGTH_LONG).show();
                        tvWorkingStartingTime.setText(apiResponse.getAnswer());
                        tvWorkingStartingTime.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                openErrorPage(21);
            }
        }
    };

    private void openErrorPage(int alertDialogNo) {
        Intent intent = new Intent(this, ErrorActivity.class);
        if (alertDialogNo == 20) {
            intent.putExtra("state", 20);
        } else if (alertDialogNo == 21) {
            intent.putExtra("state", 21);
        } else if (alertDialogNo == 22) {
            intent.putExtra("state", 22);
        }
    }

    private void checkSavedService() {
        serviceViewModel.getServiceKayitlari().observe(this, services -> {
            olusturulmusServislerListesi.addAll(services);
            if(olusturulmusServislerListesi!=null&&olusturulmusServislerListesi.size()>0){
                servisKayitlariSayfasinaGit(olusturulmusServislerListesi);
            }else {
                Toast.makeText(this, R.string.olusturulmus_servis_kaydi_bulunamadi,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void servisKayitlariSayfasinaGit(List<Service> olusturulmusServislerListesi) {
        Gson gson = new Gson();
        String userToJson = gson.toJson(user);

        Gson gson2 = new Gson();
        String serviceListToJson = gson2.toJson(olusturulmusServislerListesi);

        Intent intent = new Intent(this, ServisKaydiActivity.class);
        intent.putExtra("user", userToJson);
        intent.putExtra("service", serviceListToJson);
        startActivity(intent);
    }

    /*servis başlatma işlemlerini yapan alert dialog*/
    private void showSifirdanServisBaslatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        viewService = View.inflate(this, R.layout.alert_dialog_service, null);
        builder.setView(viewService);
        builder.setCancelable(false);
        servicesAlertDialog = builder.create();
        servicesAlertDialog.show();
        initSifirdanServisBaslatAlertDialog();
    }

    private void initSifirdanServisBaslatAlertDialog() {
        tvDateTime = viewService.findViewById(R.id.tvDateTime);
        Button btnStartService = viewService.findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(btnStartServiceListener);
        etCustomerName = viewService.findViewById(R.id.etCustomerName);
        etServisKonusu = viewService.findViewById(R.id.etServisKonusu);
        etCariYetkilisi = viewService.findViewById(R.id.etCariYetkilisi);
        autoCompleteTextView = viewService.findViewById(R.id.autoCustomer);
        autoCompleteTextView.setOnItemClickListener(autoCompleteListener);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        Button btnCloseServiceAlert=viewService.findViewById(R.id.btnCloseServiceAlert);
        btnCloseServiceAlert.setOnClickListener(v -> {
            servicesAlertDialog.dismiss();
            closeApp();
        });

        ////////////////////////////////////////////////////////////////////////////////
        getCustomers();
    }

    private AdapterView.OnItemClickListener autoCompleteListener =
            (parent, view, position, id) -> {
                etCustomerName.setText("");
                etCustomerName.setText(autoCompleteTextView.getText().toString());
            };

    private TextWatcher textWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (start == 0 && before == 1 && count == 0) {
                allCustomers.clear();
                allCustomerNames.clear();
                HideKeyboard.hide(OptionsActivity.this);
                getCustomers();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private View.OnClickListener btnStartServiceListener = v -> {
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel.class);
        serviceViewModel.init();
        beginService();
    };

    private void beginService() {
        int customerID = -1;
        String uuid = Randoms.createRandomString().toUpperCase();
        final String time = GetCurrentDate.getThisTime();
        for (int i = 0; i < allCustomers.size(); i++) {
            if (etCustomerName.getText().toString().equals(allCustomers.get(i).getCustomerName())) {
                customerID = allCustomers.get(i).getId();
            }
        }

        SifirdanServis service = new SifirdanServis();
        service.setServiceCode(uuid);
        service.setCustomerID(customerID);
        service.setIstekYapanCariYetkilisi(etCariYetkilisi.getText().toString());
        service.setIstekYapanKullaniciID(user.getUserID());
        service.setKonu(etServisKonusu.getText().toString());
        service.setIstekSaati(time);
        service.setServisBaslatanKullaniciID(user.getUserID());
        service.setBeginingDate(time);

        if (connection.isNetworkAvailable()) {
            serviceViewModel.startService(service,2, null)
                    .observe(OptionsActivity.this,
                            apiResponse -> {
                                if (apiResponse.getAnswer().equals("1")) {
                                    Toast.makeText(OptionsActivity.this,
                                            R.string.servis_baslatildi, Toast.LENGTH_SHORT).show();
                                    tvDateTime.setText(time);
                                    tvDateTime.setVisibility(View.VISIBLE);
                                    servicesAlertDialog.dismiss();
                                    checkService(user);
                                    closeApp();
                                } else {
                                    Toast.makeText(OptionsActivity.this, R.string.servis_baslatilamadi,
                                            Toast.LENGTH_LONG).show();
                                }
                            });
        } else {
            openErrorPage(20);
        }
    }

    private void closeApp() {
        showMainAlertDialog();
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

    /*ilk açılan main alert dialog işlemleri*/
    private void showMainAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        viewMain = View.inflate(this, R.layout.alert_dialog_options, null);
        initMainAlertDialog();
        builder.setView(viewMain);
        builder.setCancelable(false);
        mainAlertDialog = builder.create();
        mainAlertDialog.show();
    }

    private void initMainAlertDialog() {
        btnServisKapat = viewMain.findViewById(R.id.btn1);
        btnServisAc = viewMain.findViewById(R.id.btn2);
        btnGirisCikis = viewMain.findViewById(R.id.btn3);
        btnMain = viewMain.findViewById(R.id.btn4);
        btnIstekKaydiOlustur = viewMain.findViewById(R.id.btn5);
        btnIstekKayitlariniGetir = viewMain.findViewById(R.id.btn6);
        Button btnCikis = viewMain.findViewById(R.id.btnCikis);
        btnCikis.setOnClickListener(v -> {
            this.finish();
            System.exit(0);
        });
        Button btnGuvenliCikis = viewMain.findViewById(R.id.btnBeniUnut);
        btnGuvenliCikis.setOnClickListener(v -> guvenliCikis());

        Button[] btns = new Button[]{btnServisKapat, btnServisAc, btnGirisCikis, btnMain,
                btnIstekKaydiOlustur, btnIstekKayitlariniGetir};

        for (Button btn : btns) {
            btn.setOnClickListener(listener);
        }

        //kullanıcının başlattığı servis olup olmadığı sorgusuna göre buton pasif hale getirilir
        if (kullanicininActigiServisler == null) {
            btnServisKapat.setOnClickListener(null);
            btnServisKapat.setBackgroundColor(getResources().getColor(R.color.grey_2));
        }
    }

    private void guvenliCikis() {
        SharedPreferences preferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("user2").apply();
        this.finish();
        System.exit(0);
    }

    private void init() {
        connection = new InternetConnection(this);
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel.class);
        serviceViewModel.init();
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        customerViewModel.init();
        workingViewModel = ViewModelProviders.of(this).get(WorkingViewModel.class);
        workingViewModel.init();

        //Listeler
        kullanicininActigiServisler = new ArrayList<>();
        allCustomers = new ArrayList<>();
        olusturulmusServislerListesi = new ArrayList<>();
        allCustomerNames=new ArrayList<>();
    }

    private void getData() {
        Gson gson = new Gson();
        String userAsString = getIntent().getStringExtra("user");
        user = gson.fromJson(userAsString, User.class);
        checkWorking(user);
    }

    //kullanıcının başlattığı bir servis olup olmadığı sorgulanır
    private void checkService(User user) {
        kullanicininActigiServisler.clear();
        serviceViewModel.checkService(user).observe(
                this, services -> {
                    if (services != null) {
                        kullanicininActigiServisler.addAll(services);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        System.exit(0);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Gson gson = new Gson();
            String userToJson = gson.toJson(user);

            Gson gson2 = new Gson();
            String serviceListToJson = gson2.toJson(kullanicininActigiServisler);

            if (v == btnServisKapat) {
                mainAlertDialog.dismiss();
                if(kullanicininActigiServisler!=null&&kullanicininActigiServisler.size()>0){
                    git(userToJson, serviceListToJson);
                }else {
                    Toast.makeText(OptionsActivity.this, R.string.acik_servis_bulunamadi, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            } else if (v == btnServisAc) {
                mainAlertDialog.dismiss();
                showSifirdanServisBaslatDialog();
            } else if (v == btnIstekKayitlariniGetir) {
                checkSavedService();
            } else if (v == btnIstekKaydiOlustur) {
                Intent intent = new Intent(OptionsActivity.this, YeniServisKaydiActivity.class);
                intent.putExtra("user", userToJson);
                startActivity(intent);
            } else if (v == btnGirisCikis) {
                mainAlertDialog.dismiss();
                showWorkingAlertDialog();
            } else if (v == btnMain) {
                mainAlertDialog.dismiss();
                Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                intent.putExtra("user", userToJson);
                startActivity(intent);
            }
        }

        private void git(String userToJson, String serviceListToJson) {
            Intent intent;
            intent = new Intent(OptionsActivity.this, ServisKapatActivity.class);
            intent.putExtra("user", userToJson);
            intent.putExtra("service", serviceListToJson);
            startActivity(intent);
        }
    };
}
