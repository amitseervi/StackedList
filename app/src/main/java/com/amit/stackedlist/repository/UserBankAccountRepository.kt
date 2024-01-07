package com.amit.stackedlist.repository

import com.amit.stackedlist.model.UserBankAccountItem

interface UserBankAccountRepository {
    suspend fun fetchUserBankAccounts(): List<UserBankAccountItem>
}