package com.idyllix.bol_tech.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.idyllix.bol_tech.models.Item;
import com.idyllix.bol_tech.repositories.ItemRepository;

import java.util.List;

public class ItemViewModel extends ViewModel {
    private ItemRepository repository;

    public void init(){
        repository= ItemRepository.getInstance();
    }

    public LiveData<List<Item>> getAllItems(){
        return repository.getAllItems();
    }

}
