package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.idyllix.bol_tech.R;

public class BaslangicActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        checkUser();
    }

    private void checkUser() {
        sharedPref=getPreferences(Context.MODE_PRIVATE);
        String userAsString=sharedPref.getString("user2","");

        if (!userAsString.equals("")){
            Intent intent=new Intent(BaslangicActivity.this,OptionsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user",userAsString);
            startActivity(intent);
        }else {
            Intent intent=new Intent(BaslangicActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
