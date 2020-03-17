package com.idyllix.bol_tech.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.idyllix.bol_tech.models.ApiResponse;
import com.idyllix.bol_tech.models.Work;
import com.idyllix.bol_tech.models.WorkQuery;
import com.idyllix.bol_tech.models.WorkRequest;
import com.idyllix.bol_tech.repositories.WorkingRepository;

import java.util.List;

public class WorkingViewModel extends ViewModel {
    private WorkingRepository repository;

    public void init(){
        repository=WorkingRepository.getInstance();
    }
    public LiveData<Work> checkWorking(Work work){
        return repository.checkWorking(work);
    }
    public LiveData<ApiResponse> startWorking(WorkRequest work){
        return repository.startWorking(work);
    }
    public LiveData<ApiResponse> finishWorking(WorkRequest work){
        return repository.finishWorking(work);
    }

    public LiveData<List<Work>> getAllWorkings(WorkQuery request, int type){
        return repository.getAllWorkings(request,type);
    }
}
