package com.idyllix.bol_tech.interfaces;

import com.idyllix.bol_tech.models.Customer;

public interface CustomerClickListener {

    void onPositionLongClicked(int v, Customer customer, String data);
    void onPositionClicked(int id);

}
