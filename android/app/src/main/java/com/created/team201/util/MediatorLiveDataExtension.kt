package com.created.team201.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> MediatorLiveData<T>.addSourceList(
    vararg liveDataArgument: LiveData<*>,
    onChanged: () -> T,
) {
    liveDataArgument.forEach {
        this.addSource(it) {
            value = onChanged()
        }
    }
}

