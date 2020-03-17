package com.idyllix.bol_tech.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.adapters.CustomerAdapter;
import com.idyllix.bol_tech.interfaces.CustomerClickListener;
import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.models.Collecting;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.utils.GetCurrentDate;
import com.idyllix.bol_tech.utils.GetIntentData;
import com.idyllix.bol_tech.utils.HideKeyboard;
import com.idyllix.bol_tech.viewmodels.CustomerViewModel;
import com.idyllix.bol_tech.viewmodels.TahsilatViewModel;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CustomersReportActivity extends AppCompatActivity implements CustomerClickListener {

    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private List<Customer> customers;
    private CustomerViewModel viewModel;
    private User user;
    private AutoCompleteTextView autoCompleteTextView;
    private List<String> customerNameList;
    private ArrayAdapter<String> autoCompleteAdapter;
    private TahsilatViewModel tahsilatViewModel;

    private AlertDialog alertDialog;
    private EditText etTahsilatTutari;
    private View view;
    private static Customer longClickedCustomer;
    private EditText etAciklama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_report);

        init();
        getCustomers();
    }

    private void getCustomers() {
        viewModel.getAllCustomers().observe(this,customers1 -> {
            customers.clear();
            customerNameList.clear();
            customers.addAll(customers1);
            for(int i=0;i<customers1.size();i++){
                customerNameList.add(customers1.get(i).getCustomerName());
            }
            autoCompleteAdapter = new ArrayAdapter<>(getApplicationContext(),
                    R.layout.item_autocomplete,
                    customerNameList);

            autoCompleteTextView.setAdapter(autoCompleteAdapter);
        });
    }

    private AdapterView.OnItemClickListener
            autoCompleteListener = (parent, view, position, id) -> {
        adapter=new CustomerAdapter(this,customers,this);
        recyclerView.setAdapter(adapter);
        adapter.getFilter().filter(autoCompleteTextView.getText());
        HideKeyboard.hide(CustomersReportActivity.this);
        };

    private void init() {
        autoCompleteTextView=findViewById(R.id.autoCustomerList);
        autoCompleteTextView.setOnItemClickListener(autoCompleteListener);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        CheckBox checkBox = findViewById(R.id.checkboxCari);
        checkBox.setOnCheckedChangeListener(checkboxListener);
        recyclerView=findViewById(R.id.recyclerviewCst);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        user= GetIntentData.getUser(this);
        customers=new ArrayList<>();
        customerNameList=new ArrayList<>();
        viewModel= ViewModelProviders.of(this).get(CustomerViewModel.class);
        viewModel.init();
        tahsilatViewModel=ViewModelProviders.of(this).get(TahsilatViewModel.class);
        tahsilatViewModel.init();
    }

    private CompoundButton.OnCheckedChangeListener checkboxListener = (buttonView, isChecked) -> {
        if(isChecked){
            adapter=new CustomerAdapter(this,customers,this);
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setAdapter(null);
        }
    };

    private TextWatcher textWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (start == 0 && before == 1 && count == 0) {
                customerNameList.clear();
                customers.clear();
                HideKeyboard.hide(CustomersReportActivity.this);
                getCustomers();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public void onPositionLongClicked(int v, Customer customer, String data) {
        if(v==R.id.tvCustomerTel){
                CustomersReportActivityPermissionsDispatcher.aramaYapWithPermissionCheck(
                        CustomersReportActivity.this, 1,data);
        }else  if(v==R.id.tvCustomerBakiye){
            tahsilatYap(customer);
        }
    }

    private void tahsilatYap(Customer customer) {
        //data->telefon no
        longClickedCustomer=customer;
        showTahsilatAlertDialog();
    }

    private void showTahsilatAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog
        view = View.inflate(this, R.layout.alert_dialog_tahsilat, null);
        initTahsilatAlertDialog();
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void initTahsilatAlertDialog() {
        EditText etTahsilatCutomerName = view.findViewById(R.id.etTahsilatCustomerName);
        etTahsilatCutomerName.setText(longClickedCustomer.getCustomerName());
        etTahsilatTutari=view.findViewById(R.id.etTahsilatTutari);
        etAciklama=view.findViewById(R.id.etTahsilatAciklama);
        Button btnTahsilatYap = view.findViewById(R.id.btnTahsilatYap);
        btnTahsilatYap.setOnClickListener(v->tahsilatKaydet());
    }

    private void tahsilatKaydet() {
        Collecting collecting =new Collecting();
        collecting.setCariID(longClickedCustomer.getId());
        collecting.setOdemeMiktari(etTahsilatTutari.getText().toString().trim());
        collecting.setUserID(user.getUserID());
        collecting.setDate(GetCurrentDate.getThisTime());
        String aciklama=etAciklama.getText().toString().trim();
        if(!aciklama.equals("")){
            collecting.setAciklama(aciklama);
        }
        tahsilatViewModel.tahsilatYap(collecting).observe(this, apiResponse -> {
            if(apiResponse.getAnswer().equals("1")){
                Toast.makeText(this, R.string.tahsilat_islemi_basarili, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                getCustomers();
            }else {
                Toast.makeText(this, R.string.tahsilat_islemi_basarisiz, Toast.LENGTH_SHORT).show();
                Log.d("Hata",apiResponse.getAnswer());
            }
        });
    }

    @Override
    public void onPositionClicked(int id) {
        git(id);
    }

    private void git(int id) {
        Customer customer = new Customer();
        for(int i=0;i<customers.size();i++){
            if(customers.get(i).getId()==id){
                customer =customers.get(i);
            }
        }

        Gson gson=new Gson();
        Gson gson1=new Gson();

        Intent intent=new Intent(this,ServiceReportActivity.class);
        intent.putExtra("user",gson.toJson(user));
        intent.putExtra("customer",gson1.toJson(customer));
        intent.putExtra("reportType",100);
        startActivity(intent);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void aramaYap(int id,String data) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + data));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CustomersReportActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
    void showRationaleForCall(final PermissionRequest request){
        new AlertDialog.Builder(this)
                .setTitle("İzniniz Gerekiyor")
                .setMessage("Arama yapabilmek için izniniz gerekiyor")
                .setPositiveButton("Tamam", (dialog, which) -> request.proceed())
                .setNegativeButton("İptal", (dialog, which) -> request.cancel())
                .create()
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    void onCallPermissionDenied(){
        Toast.makeText(this, "İzin reddedildi", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    void onCameraPermissionNeverAskAgain(){
        Toast.makeText(this, "Kamerayı kullanmak için izin vermediniz. İzin vermek için yönlendirildiniz",
                Toast.LENGTH_SHORT).show();
        openAppPermissionSettings();
    }

    void openAppPermissionSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
