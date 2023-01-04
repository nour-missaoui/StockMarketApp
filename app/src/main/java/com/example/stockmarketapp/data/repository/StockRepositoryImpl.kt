package com.example.stockmarketapp.data.repository

import android.util.Log
import com.example.stockmarketapp.data.csv.CSVParser
import com.example.stockmarketapp.data.csv.CompanyListingsParser
import com.example.stockmarketapp.data.csv.IntraDayInfoParser
import com.example.stockmarketapp.data.local.StockDataBase
import com.example.stockmarketapp.data.mapper.toCompanyInfoModel
import com.example.stockmarketapp.data.mapper.toCompanyListingEntity
import com.example.stockmarketapp.data.mapper.toCompanyListingModel
import com.example.stockmarketapp.data.remote.StockApi
import com.example.stockmarketapp.domain.model.CompanyInfoModel
import com.example.stockmarketapp.domain.model.CompanyListingModel
import com.example.stockmarketapp.domain.model.IntraDayInfo
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val db: StockDataBase,
    val companyListingsParser: CSVParser<CompanyListingModel>,
    val intraDayInfoParser: CSVParser<IntraDayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListingModel() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                Log.i("aaaaaaaa", "getCompanyListings: ${response.charStream()}")
                companyListingsParser.parse(response.byteStream())

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Could not Load Data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Could not Load Data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearComapnyListings()
                dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                emit(
                    Resource.Success(
                        data = dao.searchCompanyListing("")
                            .map { it.toCompanyListingModel() })
                )
                emit(Resource.Loading(false))

            }
        }

    }

    override suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfoModel> {
       return try {
           val result = api.getCompanyInfo(symbol)
        return   Resource.Success(result.toCompanyInfoModel())

       }  catch (e: IOException) {
        e.printStackTrace()
        Resource.Error("Could not Load Data")
    } catch (e: HttpException) {
        e.printStackTrace()
        Resource.Error("Could not Load Data")
    }
    }

    override suspend fun getCompanyIntraDay(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            val response = api.getCompanyIntradayInfo(symbol)
            val result = intraDayInfoParser.parse(response.byteStream())
            return Resource.Success(result)

        }  catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Could not Load Data")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Could not Load Data")
        }
    }
}