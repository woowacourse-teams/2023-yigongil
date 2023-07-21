package com.created.domain.model

data class UserInfo(
    val userName: String,
    val myId: Int,
    val studies: List<Study>,
)
