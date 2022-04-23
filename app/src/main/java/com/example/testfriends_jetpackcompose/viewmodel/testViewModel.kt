package com.example.testfriends_jetpackcompose.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.example.testfriends_jetpackcompose.repository.ResultsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class testViewModel @Inject constructor(application: Application, resultRepo: ResultsRepo) :
    ViewModel(),
    LifecycleObserver