package com.mckj.vest.defaultwifi.ui

import androidx.lifecycle.Observer
import com.mckj.module.wifi.data.HomeRepository
import com.mckj.module.wifi.ui.BaseHomeViewModel
import com.tz.gg.appproxy.AppProxy

/**
 * Describe:
 *
 * Created By yangb on 2021/5/18
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
        AppProxy.matchLiveData.observeForever(true, matchObserver)
    }


    override fun onCleared() {
        super.onCleared()
        AppProxy.matchLiveData.removeObserver(matchObserver)
    }

}