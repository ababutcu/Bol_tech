package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.User;

public class MainActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }
    private void init() {
        getData();
        initBoxes();
    }

    private void getData() {
        Gson gson = new Gson();
        String userAsString = getIntent().getStringExtra("user");
        user=gson.fromJson(userAsString,User.class);
    }

    private void initBoxes() {

        Button btnCustomers = findViewById(R.id.btnCustomers);
        Button btnServiceReports = findViewById(R.id.btnServiceReports);
        Button btnWorkingReports = findViewById(R.id.btnWorkingReports);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnCollecting=findViewById(R.id.btnCollecting);

        Button[] buttons=new Button[]{btnCustomers, btnServiceReports,
                btnWorkingReports, btnSettings,btnCollecting};
        for(Button button:buttons){
            button.setOnClickListener(listener);
        }
    }

    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Gson gson = new Gson();
            String userToJson = gson.toJson(user);
            Intent intent=null;
            int id=v.getId();

            if(id==R.id.btnCustomers){
                intent=new Intent(MainActivity.this,CustomersReportActivity.class);
            }else if(id==R.id.btnServiceReports){
                intent=new Intent(MainActivity.this,ServiceReportActivity.class);
                intent.putExtra("reportType",200);
            }else if(id==R.id.btnWorkingReports){
                intent=new Intent(MainActivity.this, GirisCikisRaporActivity.class);
            }else if(id==R.id.btnSettings){
                intent=new Intent(MainActivity.this,SettingsActivity.class);
            }else if(id==R.id.btnCollecting){
                intent=new Intent(MainActivity.this, TahsilatActivity.class);
            }

            if (intent != null) {
                intent.putExtra("user",userToJson);
                startActivity(intent);
            }
        }
    };
}
