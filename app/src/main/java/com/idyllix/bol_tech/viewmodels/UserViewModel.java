package com.idyllix.bol_tech.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private UserRepository repository;

    public void init(){
        repository=UserRepository.getInstance();
    }

    public LiveData<User> checkUser(User user){

        //Transformations.map();
        return repository.checkUser(user);
    }

    public LiveData<List<User>> getAllUsers(){
        return repository.getAllUsers();
    }

}

