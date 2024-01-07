package com.amit.stackedlist.repository

import com.amit.stackedlist.model.UserBankAccountItem

class DummyUserBankAccountRepository : UserBankAccountRepository {
    override suspend fun fetchUserBankAccounts(): List<UserBankAccountItem> {
        return listOf(
            UserBankAccountItem("http://www.google.com", "XXXXX3812", "HDFC Bank", "12345678912"),
            UserBankAccountItem("http://www.google.com", "XXXXX1262", "Icici Bank", "2427321467")
        )
    }
}