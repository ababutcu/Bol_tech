package com.idyllix.bol_tech.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.idyllix.bol_tech.models.ApiResponse;
import com.idyllix.bol_tech.models.Collecting;
import com.idyllix.bol_tech.models.Collecting2;
import com.idyllix.bol_tech.models.CollectingQuery;
import com.idyllix.bol_tech.repositories.TahsilatRepository;

import java.util.List;

public class TahsilatViewModel extends ViewModel {

    private TahsilatRepository repository;

    public void init(){
        repository= TahsilatRepository.getInstance();
    }

    public LiveData<ApiResponse> tahsilatYap(Collecting t){
        return repository.tahsilatYap(t);
    }

    public LiveData<List<Collecting2>> getAllCollecting(CollectingQuery query, int methodNumber){
        return repository.getAllCollecting(query,methodNumber);
    }
}
