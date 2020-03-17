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
import com.idyllix.bol_tech.adapters.WorkingAdapter;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.models.Work;
import com.idyllix.bol_tech.models.WorkQuery;
import com.idyllix.bol_tech.utils.GetCurrentDate;
import com.idyllix.bol_tech.utils.InternetConnection;
import com.idyllix.bol_tech.viewmodels.UserViewModel;
import com.idyllix.bol_tech.viewmodels.WorkingViewModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GirisCikisRaporActivity extends AppCompatActivity {

    private Spinner spUserName;
    private UserViewModel userViewModel;
    private List<User> userList;
    private WorkingViewModel workingViewModel;
    private RecyclerView recyclerViewWorking;
    private WorkingAdapter adapter;
    private static int privilege;
    private List<Work> workList;
    private InternetConnection connection;
    private EditText etStart;
    private EditText etFinish;
    private User user;
    private Calendar calendar;
    private static int selectedEdittext=-1;
    private List<String> userNameList;
    private LinearLayout linearLayout;
    private CheckBox checkBox;
    private LinearLayout llSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_report);

        getData();
        init();
        initViewModels();
        getUsers();
    }

    private void initViewModels() {
        userViewModel= ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init();
        workingViewModel=ViewModelProviders.of(this).get(WorkingViewModel.class);
        workingViewModel.init();
    }

    private void init() {
        String now = GetCurrentDate.getNow();
        spUserName=findViewById(R.id.spUserName);
        recyclerViewWorking =findViewById(R.id.recyclerviewWorking);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewWorking.setHasFixedSize(true);
        recyclerViewWorking.setLayoutManager(manager);
        recyclerViewWorking.addItemDecoration(new
                DividerItemDecoration(recyclerViewWorking.getContext(),
                DividerItemDecoration.VERTICAL));
        userList =new ArrayList<>();
        workList=new ArrayList<>();
        connection=new InternetConnection(this);
        etStart=findViewById(R.id.startTime);
        etFinish=findViewById(R.id.finishTime);
        linearLayout=findViewById(R.id.llSpinnerWorkingUser);
        llSecond = findViewById(R.id.llSecond);
        Button btnGetir = findViewById(R.id.btnGetir);
        btnGetir.setOnClickListener(getirListener);

        if(privilege==2){
            llSecond.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            btnGetir.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
        }

        checkBox=findViewById(R.id.cbTumKullanicilar);
        checkBox.setOnCheckedChangeListener(checkListener);
        etStart.setText(now);
        etFinish.setText(now);
        etStart.setOnClickListener(dateListener);
        etFinish.setOnClickListener(dateListener);
    }

    private CompoundButton.OnCheckedChangeListener checkListener= (buttonView, isChecked) -> {
        if(checkBox.isChecked()){
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
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
        if(id==R.id.startTime){
            selectedEdittext=1;
        }else if(id==R.id.finishTime){
            selectedEdittext=2;
        }
        calendar = Calendar.getInstance();
        int yil = calendar.get(Calendar.YEAR);
        int ay = calendar.get(Calendar.MONTH);
        int gun = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(GirisCikisRaporActivity.this,
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

    private View.OnClickListener getirListener =v -> {
        String start =etStart.getText().toString() + " 00:00:00";
        String end =etFinish.getText().toString() + " 23:59:59";

        int selectedUserID=-1;
        for(int i=0;i<userList.size();i++){
            if(llSecond.getVisibility()==View.VISIBLE){
                if(spUserName.getSelectedItem().toString().equals(userList.get(i).getUserName())){
                    selectedUserID=userList.get(i).getUserID();
                }
            }else {
                selectedUserID=user.getUserID();
            }
        }
        if(privilege==1){
            if(checkBox.isChecked()){
                getUserReport(selectedUserID,privilege,start,end);
            }else {
                getReport(start,end);
            }
        }else {
                getUserReport(user.getUserID(),privilege,start,end);
        }
    };

    private void getUserReport(int selectedUserID, int privilege, String start, String end) {
        workList.clear();
        WorkQuery request=new WorkQuery();
        request.setUserID(selectedUserID);
        request.setYetki(privilege);
        request.setBaslangic(start);
        request.setBitis(end);
        if (connection.isNetworkAvailable()) {
            workingViewModel.getAllWorkings(request,1).observe(this, works -> {
                workList.addAll(works);
                adapter=new WorkingAdapter(workList,GirisCikisRaporActivity.this);
                recyclerViewWorking.setAdapter(adapter);
            });
        }else{
            hataSayfasinaGit();
        }
    }

    private void getData() {

        Gson gson = new Gson();
        String userAsString = getIntent().getStringExtra("user");
        user= gson.fromJson(userAsString, User.class);
        privilege = user.getPrivilege();

    }

    private void getUsers() {
        userNameList=new ArrayList<>();
        if(privilege==1){
            userViewModel.getAllUsers().observe(this, users -> {
                userList.addAll(users);
                for (int i=0;i<users.size();i++){
                    userNameList.add(users.get(i).getUserName());
                }
            });
        }else{
            userList.add(user);
            userNameList.add(user.getUserName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                GirisCikisRaporActivity.this, android.R.layout.simple_spinner_dropdown_item, userNameList);

        spUserName.setAdapter(adapter);
        spUserName.setSelection(0);
        adapter.notifyDataSetChanged();
    }

    private void hataSayfasinaGit(){
        Intent intent=new Intent(this,ErrorActivity.class);
        intent.putExtra("activity","working_report");
        startActivity(intent);
    }

    private void getReport(String start, String end) {
        workList.clear();
        WorkQuery request=new WorkQuery();
        request.setBaslangic(start);
        request.setBitis(end);
        if (connection.isNetworkAvailable()) {

            workingViewModel.getAllWorkings(request,0).observe(this, works -> {
                workList.addAll(works);
                adapter=new WorkingAdapter(workList,GirisCikisRaporActivity.this);
                recyclerViewWorking.setAdapter(adapter);
            });
        }else{
            hataSayfasinaGit();
        }
    }
}
