package com.amit.stackedlist.model.ui

data class EmiRateOptionPresentationItem constructor(
    val id: String,
    val duration: String,
    val amountPerMonth: String,
    val selected: Boolean = false
)