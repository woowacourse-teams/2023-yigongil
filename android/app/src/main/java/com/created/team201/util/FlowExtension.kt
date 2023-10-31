package com.created.team201.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.collectOnStarted(
    lifecycleOwner: LifecycleOwner,
    crossinline action: (T) -> Unit,
) {
    lifecycleOwner.lifecycleScope.launch {
        flowWithLifecycle(lifecycleOwner.lifecycle).collect { value ->
            action(value)
        }
    }
}

inline fun <reified T> Flow<T>.collectLatestOnStarted(
    lifecycleOwner: LifecycleOwner,
    crossinline action: (T) -> Unit,
) {
    lifecycleOwner.lifecycleScope.launch {
        flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest { value ->
            action(value)
        }
    }
}
