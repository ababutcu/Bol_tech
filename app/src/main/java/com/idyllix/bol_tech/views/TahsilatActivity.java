package com.idyllix.boltech.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.idyllix.boltech.R;
import com.idyllix.boltech.adapters.TahsilatAdapter;
import com.idyllix.boltech.models.Collecting;
import com.idyllix.boltech.models.Collecting2;
import com.idyllix.boltech.models.CollectingQuery;
import com.idyllix.boltech.models.Customer;
import com.idyllix.boltech.models.User;
import com.idyllix.boltech.utils.GetCurrentDate;
import com.idyllix.boltech.utils.HideKeyboard;
import com.idyllix.boltech.viewmodels.CustomerViewModel;
import com.idyllix.boltech.viewmodels.TahsilatViewModel;
import com.idyllix.boltech.viewmodels.UserViewModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TahsilatActivity extends AppCompatActivity {

    private TahsilatViewModel tahsilatViewModel;
    private CustomerViewModel customerViewModel;
    private UserViewModel userViewModel;
    private List<Collecting2> collectingList;
    private List<User> userList;
    private List<String> userNameList;
    private List<Customer> allCustomers;
    private List<String> allCustomerNames;
    private RecyclerView recyclerView;
    private TahsilatAdapter adapter;
    private User user;
    private static int selectedEdittext=-1;
    private CheckBox cbCustomers;
    private EditText etStart;
    private EditText etFinish;
    private Calendar calendar;
    private int privilege;
    private LinearLayout llUser;
    private CheckBox cbUsers;
    private View view;
    private AlertDialog alertDialog;
    private AutoCompleteTextView autoCompleteTextView;
    private Spinner spUserName;
    private EditText etCustomerName;
    private ArrayAdapter<String> autoCompleteAdapter;
    private boolean useCustomer=true;
    private boolean useUser=true;
    private String userAsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tahsilat);

        getData();
        initViews();
        init();
        showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        view = View.inflate(this, R.layout.alert_dialog_tahsilat_report, null);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        initAlertDialog();
    }

    private void initAlertDialog() {
        etStart=view.findViewById(R.id.etBaslangic);
        etStart.setText(GetCurrentDate.getNow());
        etFinish=view.findViewById(R.id.etBitis);
        etFinish.setText(GetCurrentDate.getNow());
        etStart.setOnClickListener(dateListener);
        etFinish.setOnClickListener(dateListener);
        autoCompleteTextView=view.findViewById(R.id.autoCustomer);
        autoCompleteTextView.setOnItemClickListener(autoCompleteListener);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        spUserName=view.findViewById(R.id.spUserName);
        cbCustomers =view.findViewById(R.id.cbTahsilatTumCariler);
        cbCustomers.setOnCheckedChangeListener(customerCheckedListener);
        cbUsers =view.findViewById(R.id.cbTahsilatTumKullanicilar);
        cbUsers.setOnCheckedChangeListener(userCheckedListener);
        llUser=view.findViewById(R.id.llUser);
        Button btnGetir = view.findViewById(R.id.btnGetir);
        btnGetir.setOnClickListener(v -> getList());
        Button btnKapat=view.findViewById(R.id.btnKapat);
        btnKapat.setOnClickListener(v -> kapat());
        etCustomerName=view.findViewById(R.id.etCustomerName);
        if(privilege==2){
            llUser.setVisibility(View.GONE);
            cbUsers.setVisibility(View.GONE);
        }
        getCustomers();
        getUsers();
    }

    private void kapat() {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("user",userAsString);
        startActivity(intent);
        this.finish();
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
                HideKeyboard.hide(TahsilatActivity.this);
                getCustomers();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

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

    private void getUsers(){
        userViewModel.getAllUsers().observe(this,users -> {
            userList.addAll(users);
            for(int i=0;i<users.size();i++){
                userNameList.add(users.get(i).getUserName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(TahsilatActivity.this,
                    android.R.layout.simple_list_item_1, userNameList);
            spUserName.setAdapter(adapter);
        });
    }

    private void init() {
        tahsilatViewModel= ViewModelProviders.of(this).get(TahsilatViewModel.class);
        tahsilatViewModel.init();
        userViewModel=ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init();
        customerViewModel=ViewModelProviders.of(this).get(CustomerViewModel.class);
        customerViewModel.init();

        collectingList=new ArrayList<>();
        allCustomerNames=new ArrayList<>();
        allCustomers=new ArrayList<>();
        userList=new ArrayList<>();
        userNameList=new ArrayList<>();
    }

    private void initViews() {
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void getData() {
        Gson gson=new Gson();
        userAsString=getIntent().getStringExtra("user");
        user= gson.fromJson(userAsString, User.class);
        privilege = user.getPrivilege();
    }

    private void getList() {
        CollectingQuery query=new CollectingQuery();
        int methodNumber;
        if(useCustomer&&useUser){
            for(int i=0;i<allCustomers.size();i++){
                if(etCustomerName.getText().toString().trim().equals(allCustomers.get(i).getCustomerName())){
                    query.setCustomerID(allCustomers.get(i).getId());
                }
            }
            for(int i=0;i<userList.size();i++){
                if(spUserName.getSelectedItem().toString().equals(userList.get(i).getUserName())){
                    query.setUserID(user.getUserID());
                }
            }
            methodNumber=1;
            Log.d("TahsilatRapor","1");
        }else if(!useUser&&useCustomer){
            for(int i=0;i<allCustomers.size();i++){
                if(etCustomerName.getText().toString().trim().equals(allCustomers.get(i).getCustomerName())){
                    query.setCustomerID(allCustomers.get(i).getId());
                }
            }
            methodNumber=2;
            Log.d("TahsilatRapor","2");
        }else if(useUser){
            for(int i=0;i<userList.size();i++){
                if(spUserName.getSelectedItem().toString().equals(userList.get(i).getUserName())){
                    query.setUserID(user.getUserID());
                }
            }
            methodNumber=3;
            Log.d("TahsilatRapor","3");
        }else {
            methodNumber=4;
            Log.d("TahsilatRapor","4");
        }

        String start = etStart.getText().toString() + " 00:00:00";
        String end = etFinish.getText().toString() + " 23:59:59";
        query.setBaslangic(start);
        query.setBitis(end);
        tahsilatViewModel.getAllCollecting(query,methodNumber).observe(this, collectings -> {
            collectingList.addAll(collectings);
            adapter=new TahsilatAdapter(this,collectingList);
            recyclerView.setAdapter(adapter);
            alertDialog.dismiss();
        });
    }

    private CompoundButton.OnCheckedChangeListener customerCheckedListener =
            (buttonView, isChecked) -> {
                if(cbCustomers.isChecked()){
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                    etCustomerName.setVisibility(View.VISIBLE);
                    useCustomer=true;
                }else {
                    autoCompleteTextView.setVisibility(View.GONE);
                    etCustomerName.setVisibility(View.GONE);
                    useCustomer=false;
                }
            };

    private CompoundButton.OnCheckedChangeListener userCheckedListener=
            (buttonView, isChecked) -> {
                if(cbUsers.isChecked()){
                    llUser.setVisibility(View.VISIBLE);
                    useUser=true;
                }else {
                    llUser.setVisibility(View.GONE);
                    useUser=false;
                }
            };

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(TahsilatActivity.this,
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
