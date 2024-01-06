package com.amit.stackedlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

const val DUMMY_USER_NAME = "Amit"
const val MINIMUM_CREDIT_AMOUNT = 10
const val MAXIMUM_CREDIT_AMOUNT = 10000

class HomeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _creditLimit =
        MutableStateFlow<Pair<Int, Int>>(MINIMUM_CREDIT_AMOUNT to MAXIMUM_CREDIT_AMOUNT)
    val creditLimit: StateFlow<Pair<Int, Int>>
        get() = _creditLimit

    private val _selectedCredit: MutableStateFlow<Int> = MutableStateFlow(MINIMUM_CREDIT_AMOUNT)
    val selectedCredit: StateFlow<Int>
        get() = _selectedCredit

    init {
        _creditLimit.update {
            MINIMUM_CREDIT_AMOUNT to MAXIMUM_CREDIT_AMOUNT
        }
    }


    // amountSeeker = 0 to 100
    fun onEmiAmountSeekerChange(amountSeeker: Int) {
        _selectedCredit.update {
            maxOf(amountSeeker * (MAXIMUM_CREDIT_AMOUNT / 100), MINIMUM_CREDIT_AMOUNT)
        }
    }

    class Factory(private val application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }
}