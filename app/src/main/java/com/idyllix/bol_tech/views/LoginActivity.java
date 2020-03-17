package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.utils.InternetConnection;
import com.idyllix.bol_tech.viewmodels.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etPassword;
    private UserViewModel viewModel;
    private InternetConnection connection;
    private static int error = 0;
    private CheckBox checkBox;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        checkUser();
    }

    private void checkUser() {
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        String userAsString = sharedPref.getString("user2", "");

        if(!userAsString.equals("")){
            Gson gson = new Gson();
            User user = gson.fromJson(userAsString, User.class);
            etUserName.setText(user.getUserName());
            etPassword.setText(user.getPassword());
        }
    }
        private void init() {
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        checkBox = findViewById(R.id.cbRememberMe);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(listener);
        connection = new InternetConnection(this);
        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.init();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String userName = etUserName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (checkEmpty(userName, password)) {
                if (connection.isNetworkAvailable()) {
                    viewModel.checkUser(new User(userName, password)).observe(LoginActivity.this,
                            user -> {
                                if (userName.equals(user.getUserName()) && password.equals(user.getPassword())) {
                                    throughToMainActivity(user);
                                }else{
                                    Toast.makeText(LoginActivity.this, R.string.bilgiler_yanlis_user,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Intent intent = new Intent(LoginActivity.this, ErrorActivity.class);
                    intent.putExtra("activity", "login");
                    startActivity(intent);
                }
            } else {
                if (error == 1) {
                    etUserName.setError(getString(R.string.kullanici_adi_bos_birakilamaz));
                } else if (error == 2) {
                    etPassword.setError(getString(R.string.sifre_bos_birakilamaz));
                } else if (error == 3) {
                    etUserName.setError(getString(R.string.kullanici_adi_bos_birakilamaz));
                    etPassword.setError(getString(R.string.sifre_bos_birakilamaz));
                }
            }
        }
    };

    private void checkRemember(String userToJson) {
        if (checkBox.isChecked()) {
            sharedPref = getPreferences(Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            editor.putString("user2", userToJson);
            editor.apply();
        }
    }

    private void throughToMainActivity(User user) {

        Gson gson = new Gson();
        String userToJson = gson.toJson(user);
        checkRemember(userToJson);

        Intent intent = new Intent(LoginActivity.this, OptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("user", userToJson);
        startActivity(intent);
    }

    private boolean checkEmpty(String userName, String password) {
        String s = "^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9]{4,12}$";

        if (userName.equals("") && password.equals("")) {
            error = 3;
            return false;
        } else if (userName.equals("") && !password.trim().equals("")) {
            error = 1;
            return false;
        } else if (password.equals("")) {
            error = 2;
            return false;
        } else {
            return true;
        }
    }
}
