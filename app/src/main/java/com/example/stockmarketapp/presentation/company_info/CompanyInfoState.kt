package com.example.stockmarketapp.presentation.company_info

import com.example.stockmarketapp.domain.model.CompanyInfoModel
import com.example.stockmarketapp.domain.model.IntraDayInfo

data class CompanyInfoState (
    val StockInfos : List<IntraDayInfo>? = emptyList(),
    val company : CompanyInfoModel? = null,
    val isloading : Boolean = false,
    val error : String? = null
        )