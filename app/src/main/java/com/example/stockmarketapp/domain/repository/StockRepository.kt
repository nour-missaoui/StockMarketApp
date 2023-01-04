package com.example.stockmarketapp.domain.repository

import com.example.stockmarketapp.domain.model.CompanyInfoModel
import com.example.stockmarketapp.domain.model.CompanyListingModel
import com.example.stockmarketapp.domain.model.IntraDayInfo
import com.example.stockmarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>>

    suspend fun getCompanyInfo(
        symbol: String,
        ): Resource<CompanyInfoModel>

    suspend fun getCompanyIntraDay(
        symbol: String,
    ): Resource<List<IntraDayInfo>>
}