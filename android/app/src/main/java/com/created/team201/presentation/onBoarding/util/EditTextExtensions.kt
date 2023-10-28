package com.created.team201.presentation.onBoarding.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun EditText.textChangesToFlow(): Flow<CharSequence?> {
    return callbackFlow {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(p0: Editable?) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                trySend(p0)
            }
        }

        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart {
        emit(text)
    }
}
