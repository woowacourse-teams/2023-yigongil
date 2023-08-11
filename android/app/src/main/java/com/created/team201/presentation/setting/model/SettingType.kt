package com.created.team201.presentation.setting.model

enum class SettingType(val itemId: Long) {
    NOTIFICATION(0L),
    ACCOUNT(1L),
    POLICY(2L),
    LOGOUT(3L),
    ;

    companion object {
        fun valueOf(itemId: Long): SettingType =
            when (itemId) {
                0L -> NOTIFICATION
                1L -> ACCOUNT
                2L -> POLICY
                3L -> LOGOUT
                else -> throw IllegalArgumentException()
            }
    }
}
