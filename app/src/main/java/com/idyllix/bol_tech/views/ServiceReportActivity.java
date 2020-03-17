package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.adapters.ServiceAdapter;
import com.idyllix.bol_tech.interfaces.ClickListener;
import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.ServiceRequest;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.utils.GetCurrentDate;
import com.idyllix.bol_tech.utils.GetIntentData;
import com.idyllix.bol_tech.utils.InternetConnection;
import com.idyllix.bol_tech.viewmodels.CustomerViewModel;
import com.idyllix.bol_tech.viewmodels.ServiceViewModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServiceReportActivity extends AppCompatActivity implements ClickListener {

    private InternetConnection connection;
    private RecyclerView recyclerView;
    private List<Service> serviceList;

    private ServiceViewModel serviceViewModel;
    private ServiceAdapter adapter;
    private Spinner spCustomerNames;
    private CustomerViewModel customerViewModel;
    private List<Customer> allCustomers;
    private List<String> customerNameList;
    private static int privilege;
    private static int userID;
    private User user;
    private Customer customer;
    private EditText etStart;
    private EditText etFinish;
    private EditText etCustomer;
    private Button btnGetir;
    private LinearLayout linearLayout;
    private Calendar calendar;
    private static int selectedEdittext=-1;
    private static int reportType;
    private CheckBox checkBox;
    private LinearLayout llSecondRow;
    private int cariID=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_report);

        getData();
        init();

        if(customer!=null){
            llSecondRow.setVisibility(View.GONE);
            etCustomer.setText(customer.getCustomerName());
        }else {
            etCustomer.setVisibility(View.GONE);
            btnGetir.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
            getCustomers();
        }
    }

    private void getData() {
        Gson gson = new Gson();
        String userAsString = getIntent().getStringExtra("user");
        reportType=getIntent().getIntExtra("reportType",-1);
        user = gson.fromJson(userAsString, User.class);
        privilege = user.getPrivilege();
        userID = user.getUserID();
        customer=GetIntentData.getCustomer(this);
        if(customer!=null){
            cariID=customer.getId();
        }
    }

    private void init() {
        connection = new InternetConnection(this);
        serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel.class);
        serviceViewModel.init();
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        customerViewModel.init();

        serviceList = new ArrayList<>();
        allCustomers = new ArrayList<>();
        customerNameList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        spCustomerNames = findViewById(R.id.spCustomerName);

        etStart=findViewById(R.id.etBaslangic);
        etStart.setText(GetCurrentDate.getNow());
        etFinish=findViewById(R.id.etBitis);
        etFinish.setText(GetCurrentDate.getNow());
        etStart.setOnClickListener(dateListener);
        etFinish.setOnClickListener(dateListener);
        etCustomer=findViewById(R.id.etCustomer);
        btnGetir=findViewById(R.id.btnGetir);
        btnGetir.setOnClickListener(listener);
        linearLayout=findViewById(R.id.llCustomer);
        checkBox=findViewById(R.id.cbServiceTumCariler);
        checkBox.setOnCheckedChangeListener(checkedListener);
        llSecondRow=findViewById(R.id.llSecond);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private CompoundButton.OnCheckedChangeListener checkedListener =
            (buttonView, isChecked) -> {
        if(checkBox.isChecked()){
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }
    };

    private void getCustomers() {
        if(connection.isNetworkAvailable()){
            customerViewModel.getCustomers().observe(this, customers -> {

                        allCustomers.addAll(customers);
                        for (int i = 0; i < customers.size(); i++) {
                            customerNameList.add(customers.get(i).getCustomerName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                ServiceReportActivity.this, android.R.layout.simple_spinner_dropdown_item, customerNameList);

                        spCustomerNames.setAdapter(adapter);
                        spCustomerNames.setSelection(0);
                    }
            );
        }else{
            hataSayfasinaGit();
        }
    }

    private View.OnClickListener listener=v -> {

        if(reportType==200){
            if(checkBox.isChecked()){
                getCustomerServices();
            }else {
                getServices();
            }
        }else if(reportType==100) {
            getCustomerServices();
        }
    };

    private void getCustomerServices() {
        String sDate0= etStart.getText().toString() + " 00:00:00";
        String sDate1= etFinish.getText().toString() + " 23:59:59";

        ServiceRequest request=new ServiceRequest();
        request.setUserID(userID);

        for (int i=0;i<allCustomers.size();i++){
            if(spCustomerNames.getSelectedItem().toString().equals(allCustomers.get(i).getCustomerName())){
                cariID=allCustomers.get(i).getId();
            }
        }
        request.setCustomerID(cariID);
        request.setPrivilege(privilege);
        request.setBaslangic(sDate0);
        request.setBitis(sDate1);

        serviceViewModel.getCustomerService(request,1).observe(this,services -> {
            serviceList.clear();
            serviceList.addAll(services);
            adapter = new ServiceAdapter(serviceList,
                    ServiceReportActivity.this);
            recyclerView.setAdapter(adapter);
        });
    }

    private void getServices() {
        String sDate0= etStart.getText().toString() + " 00:00:00";
        String sDate1= etFinish.getText().toString() + " 23:59:59";

        ServiceRequest serviceRequest=new ServiceRequest();
        serviceRequest.setUserID(userID);
        serviceRequest.setPrivilege(privilege);
        serviceRequest.setBaslangic(sDate0);
        serviceRequest.setBitis(sDate1);

      if(connection.isNetworkAvailable()){
          serviceList.clear();
                serviceViewModel.getServices(serviceRequest,2)
                        .observe(this, services -> {
                            serviceList.addAll(services);
                            adapter = new ServiceAdapter(serviceList,
                                    ServiceReportActivity.this);
                            recyclerView.setAdapter(adapter);
                        });
        }else{
            hataSayfasinaGit();
        }
    }

    //TODO sayfa hazırlanacak
    @Override
    public void onPositionClicked(int position) {
        if (connection.isNetworkAvailable()) {
            Gson gson = new Gson();
            Gson gson1=new Gson();
            String userToJson = gson.toJson(user);
            String serviceToJson=gson1.toJson(serviceList.get(position));
            Intent intent = new Intent(ServiceReportActivity.this, AyrintiliServisActivity.class);
            intent.putExtra("service",serviceToJson);
            intent.putExtra("user",userToJson);
            startActivity(intent);
        }else {
            hataSayfasinaGit();
        }
    }

    private void hataSayfasinaGit(){
        Intent intent=new Intent(this,ErrorActivity.class);
        intent.putExtra("activity","service_report");
        startActivity(intent);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener
            = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(year, month, dayOfMonth);
                    String myFormat = "dd.MM.yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("tr"));
                    if(selectedEdittext==1){
                        etStart.setText(sdf.format(calendar.getTime()));
                    }else if(selectedEdittext==2){
                        etFinish.setText(sdf.format(calendar.getTime()));
                    }
                }
            };

    private View.OnClickListener dateListener= v -> {
        int id=v.getId();
        if(id==R.id.etBaslangic){
            selectedEdittext=1;
        }else {
            selectedEdittext=2;
        }
        calendar = Calendar.getInstance();
        int yil = calendar.get(Calendar.YEAR);
        int ay = calendar.get(Calendar.MONTH);
        int gun = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ServiceReportActivity.this,
                dateSetListener, yil, ay, gun);

        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("tr"));
        Date d;
        if(selectedEdittext==2){
            try {
                d = sdf.parse(etStart.getText().toString());
                if (d != null) {
                    long milliseconds = d.getTime();
                    datePickerDialog.getDatePicker().setMinDate(milliseconds);
                    datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(selectedEdittext==1) {
            long milliseconds=calendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(milliseconds);
        }
        datePickerDialog.show();
    };
}
