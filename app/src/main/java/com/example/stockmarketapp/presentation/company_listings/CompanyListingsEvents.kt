package com.example.stockmarketapp.presentation.company_listings

import androidx.room.Query

sealed class CompanyListingsEvents{
    object Refresh : CompanyListingsEvents()
    data class OnSearchQueryChange(val query: String): CompanyListingsEvents()
}
