package com.mckj.vest.greenacleanup.ui

import androidx.lifecycle.Observer
import com.mckj.module.cleanup.data.HomeRepository
import com.mckj.module.cleanup.ui.BaseHomeViewModel


/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
class HomeViewModel(override val repository: HomeRepository) : BaseHomeViewModel(repository) {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val matchObserver = Observer<Boolean> { result ->
        loadHomeData()
    }

    init {
        loadHomeData()

    }


    override fun onCleared() {
        super.onCleared()
    }

}