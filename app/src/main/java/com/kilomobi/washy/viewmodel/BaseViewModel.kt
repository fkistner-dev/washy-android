/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewmodel

import androidx.lifecycle.ViewModel
import com.kilomobi.washy.repo.BaseRepository

abstract class BaseViewModel(repository: BaseRepository) : ViewModel() {
    var isLoading =  repository.isLoading
}