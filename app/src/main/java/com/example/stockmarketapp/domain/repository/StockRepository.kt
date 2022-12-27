package com.example.stockmarketapp.domain.repository

import androidx.room.Query
import com.example.stockmarketapp.domain.model.CompanyListingModel
import com.example.stockmarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>>
}