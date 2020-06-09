package com.kilomobi.washy.viewmodel

import androidx.lifecycle.ViewModel
import com.kilomobi.washy.repo.BaseRepository

abstract class BaseViewModel(repository: BaseRepository) : ViewModel() {
    var isLoading =  repository.isLoading
}