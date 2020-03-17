package com.idyllix.bol_tech.utils;

import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idyllix.bol_tech.models.Customer;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.User;
import java.lang.reflect.Type;
import java.util.List;

public class GetIntentData {

    public static User getUser(Activity context) {
        Gson gson = new Gson();
        String userAsString = context.getIntent().getStringExtra("user");
        return gson.fromJson(userAsString, User.class);
    }

    public static List<Service> getService(Activity activity){
        Gson gson2 = new Gson();
        Type listType = new TypeToken<List<Service>>() {
        }.getType();
        String serviceListAsString = activity.getIntent().getStringExtra("service");

        return gson2.fromJson(serviceListAsString, listType);
    }

    public static String toJson(User user){
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    public static String CustomertoJson(Customer customer){
        Gson gson = new Gson();
        return gson.toJson(customer);
    }

    public static Customer getCustomer(Activity activity){
        Gson gson = new Gson();
        String customerAsString = activity.getIntent().getStringExtra("customer");
        return gson.fromJson(customerAsString,Customer.class);
    }
}
