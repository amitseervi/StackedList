package com.amit.stackedlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amit.stackedlist.model.ui.EmiRateOptionItem
import com.amit.stackedlist.model.ui.EmiRateOptionPresentationItem
import com.amit.stackedlist.repository.EmiRateOptionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency

private const val INTEREST_MULTIPLIER = 1.2f

class HomeViewModel(
    private val application: Application,
    private val emiRateOptionRepository: EmiRateOptionsRepository
) : AndroidViewModel(application) {

    private val _selectedCreditValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _emiRateOptionList: MutableStateFlow<List<EmiRateOptionItem>> =
        MutableStateFlow(
            emptyList()
        )

    private val _userFormStep: MutableStateFlow<UserFormStep> =
        MutableStateFlow(UserFormStep.SelectCreditAmount)


    private val _emiRateSelectedOption: MutableStateFlow<String?> =
        MutableStateFlow(null)

    private val _selectedBankAccountOptionId: MutableStateFlow<String?> = MutableStateFlow(null)

    val emiRateSelectedOption: StateFlow<EmiRateOptionPresentationItem?>
        get() = combine(
            _emiRateSelectedOption,
            _emiRateOptionList
        ) { selectedOptionId: String?, optionList: List<EmiRateOptionItem> ->
            optionList.find { it.id == selectedOptionId }?.let {
                transformEmiRateOptionToPresentation(it, true)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val selectedBankAccountOptionId: StateFlow<String?>
        get() = _selectedBankAccountOptionId

    val ctaButtonMessage: StateFlow<String>
        get() = _userFormStep.map {
            when (it) {
                UserFormStep.SelectBankAccount -> application.getString(R.string.tap_for_kyc)
                UserFormStep.SelectCreditAmount -> application.getString(R.string.proceed_to_emi_option)
                UserFormStep.SelectEmiRateOption -> application.getString(R.string.select_your_bank_account)
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            application.getString(R.string.proceed_to_emi_option)
        )

    val emiRateOptionList: StateFlow<List<EmiRateOptionPresentationItem>>
        get() = combine(
            _emiRateOptionList,
            _emiRateSelectedOption
        ) { optionDataList, selectedOption ->
            optionDataList.map { optionData ->
                transformEmiRateOptionToPresentation(
                    optionData,
                    selectedOption == optionData.id
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly, emptyList()
        )
    val ctaButtonEnabled: StateFlow<Boolean>
        get() = combine(
            _userFormStep,
            _selectedCreditValue,
            _emiRateSelectedOption,
            _selectedBankAccountOptionId
        ) { userFormStep, selectedCreditAmount, emiRateOption, bankOptionId ->
            when (userFormStep) {
                UserFormStep.SelectBankAccount -> !bankOptionId.isNullOrBlank()
                UserFormStep.SelectCreditAmount -> selectedCreditAmount > 0
                UserFormStep.SelectEmiRateOption -> emiRateOption != null
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
            ((it * 10) + numberValue).coerceAtMost(1000000)
        }
    }

    fun onCreditInputBackSpacePressed() {
        _selectedCreditValue.update {
            it / 10
        }
    }

    fun onEmiValueSelectSectionVisible() {
        _userFormStep.update {
            UserFormStep.SelectCreditAmount
        }
    }

    fun onEmiRateOptionSelectSectionVisible() {
        _userFormStep.update {
            UserFormStep.SelectEmiRateOption
        }
        refreshEmiRateOptionList()
    }

    fun onBankAccountSelectSectionVisible() {
        _userFormStep.update {
            UserFormStep.SelectBankAccount
        }
    }

    private fun refreshEmiRateOptionList() {
        viewModelScope.launch {
            val options = emiRateOptionRepository.fetchEmiRateOptions(_selectedCreditValue.value)
            _emiRateOptionList.emit(options)
        }
    }

    fun onEmiRateOptionSelected(emiRateOptionItem: EmiRateOptionPresentationItem) {
        _emiRateSelectedOption.update { emiRateOptionItem.id }
    }

    class Factory(
        private val application: Application,
        private val emiRateOptionsRepository: EmiRateOptionsRepository
    ) :
        ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(application, emiRateOptionsRepository) as T
        }
    }

    private fun transformEmiRateOptionToPresentation(
        emiRateOptionItem: EmiRateOptionItem,
        selected: Boolean
    ): EmiRateOptionPresentationItem {
        return EmiRateOptionPresentationItem(
            id = emiRateOptionItem.id,
            duration = application.getString(
                R.string.emi_per_month_duration,
                emiRateOptionItem.duration.toString()
            ),
            amountPerMonth = currencyFormatter.format(emiRateOptionItem.amountPerMonth),
            selected = selected
        )
    }

    sealed interface UserFormStep {
        data object SelectCreditAmount : UserFormStep
        data object SelectEmiRateOption : UserFormStep
        data object SelectBankAccount : UserFormStep
    }
}