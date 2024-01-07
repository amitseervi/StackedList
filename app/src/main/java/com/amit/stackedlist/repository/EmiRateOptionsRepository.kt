package com.amit.stackedlist.repository

import com.amit.stackedlist.model.EmiRateOptionItem

interface EmiRateOptionsRepository {
    suspend fun fetchEmiRateOptions(creditAmount: Int): List<EmiRateOptionItem>
}