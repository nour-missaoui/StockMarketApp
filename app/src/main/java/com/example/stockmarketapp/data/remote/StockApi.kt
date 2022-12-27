package com.example.stockmarketapp.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apiKey") apiKey: String = API_KEY
    ): ResponseBody

    companion object{
        const val API_KEY = "88812232C6PHIU77"
        const val BASE_URL = "https://www.alphavantage.co"
    }
}