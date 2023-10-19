package com.created.team201.presentation.studyThread.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.created.team201.R

enum class WarningType(
    @StringRes val title: Int,
    @StringRes val content: Int,
    @StringRes val acceptTitle: Int,
    @ColorRes val acceptButtonColor: Int,
) {
    QUIT(
        R.string.thread_warning_dialog_title_quit,
        R.string.thread_warning_dialog_content_quit,
        R.string.thread_warning_dialog_accept_quit,
        R.color.red_EB7A7A,
    ),
    END(
        R.string.thread_warning_dialog_title_end,
        R.string.thread_warning_dialog_content_end,
        R.string.thread_warning_dialog_accept_end,
        R.color.green05_3AD353,
    ),
    ;

    companion object {
        fun of(isMaster: Boolean): WarningType = when (isMaster) {
            true -> END
            false -> QUIT
        }
    }
}
