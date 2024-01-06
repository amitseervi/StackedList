package com.amit.stackedlist.repository

import com.amit.stackedlist.model.ui.EmiRateOptionItem

interface EmiRateOptionsRepository {
    suspend fun fetchEmiRateOptions(creditAmount: Int): List<EmiRateOptionItem>
}