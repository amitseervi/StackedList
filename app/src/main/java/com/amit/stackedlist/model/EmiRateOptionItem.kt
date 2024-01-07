package com.amit.stackedlist.model

data class EmiRateOptionItem(
    val id: String,
    val duration: Int, //In months
    val amountPerMonth: Int //In Inr
)