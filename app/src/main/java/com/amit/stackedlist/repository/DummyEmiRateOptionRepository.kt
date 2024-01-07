package com.amit.stackedlist.repository

import com.amit.stackedlist.model.EmiRateOptionItem
import kotlin.math.ceil
import kotlin.math.roundToInt

private data class CreditMultiplier(val duration: Int, val multiplier: Float)
class DummyEmiRateOptionRepository :
    EmiRateOptionsRepository {
    private val creditMultipliers = listOf<CreditMultiplier>(
        CreditMultiplier(24, 1.5f),
        CreditMultiplier(12, 1.4f),
        CreditMultiplier(6, 1.3f),
        CreditMultiplier(3, 1.2f)
    )

    override suspend fun fetchEmiRateOptions(creditAmount: Int): List<EmiRateOptionItem> {
        return creditMultipliers.map {
            EmiRateOptionItem(
                id = "emi_rate_option_${it.duration}",
                duration = it.duration,
                amountPerMonth = ceil((creditAmount.toFloat() * it.multiplier) / it.duration).roundToInt()
            )
        }
    }
}