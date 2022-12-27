package com.example.stockmarketapp.presentation.company_listings

import com.example.stockmarketapp.domain.model.CompanyListingModel

data class CompanyListingsState(
     val companies : List<CompanyListingModel> = emptyList(),
     val isLoding : Boolean = false,
     val isRefreching : Boolean = false,
     val searchQuery : String = ""
)
