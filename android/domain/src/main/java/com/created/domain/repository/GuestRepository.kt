package com.created.domain.repository

interface GuestRepository {
    fun signUpGuest()
    fun getIsGuest(): Boolean
}
