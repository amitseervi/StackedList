package com.amit.stackedlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Currency


class HomeViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _selectedCreditValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _ctaButtonText: MutableStateFlow<String> = MutableStateFlow(
        application.getString(R.string.proceed_to_emi_option)
    )

    val ctaButtonMessage: StateFlow<String>
        get() = _ctaButtonText
    val ctaButtonEnabled: StateFlow<Boolean>
        get() = _selectedCreditValue.map { it > 0 }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val currencyFormatter by lazy {
        NumberFormat.getCurrencyInstance().apply {
            this.maximumFractionDigits = 0
            this.currency = Currency.getInstance("INR")
        }
    }
    val selectedCredit: StateFlow<String>
        get() = _selectedCreditValue.map { currencyFormatter.format(it) }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly, application.getString(R.string.rupee_value, "0")
        )

    fun onCreditInputNumberPressed(numberValue: Int) {
        _selectedCreditValue.update {
            (it * 10) + numberValue
        }
    }

    fun onCreditInputBackSpacePressed() {
        _selectedCreditValue.update {
            it / 10
        }
    }

    fun onEmiValueSelectSectionVisible() {
        _ctaButtonText.update {
            application.getString(R.string.proceed_to_emi_option)
        }
    }

    fun onEmiRateOptionSelectSectionVisible() {
        _ctaButtonText.update {
            application.getString(R.string.select_your_bank_account)
        }
    }

    fun onBankAccountSelectSetionVisible() {
        _ctaButtonText.update {
            application.getString(R.string.tap_for_kyc)
        }
    }

    class Factory(private val application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }
}