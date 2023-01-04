package com.example.stockmarketapp.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

init {
    viewModelScope.launch {
        val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
        state = state.copy(isloading = true)
        val compnayInfoResult = async{ repository.getCompanyInfo(symbol) }
        val intraDayInfo = async { repository.getCompanyIntraDay(symbol) }
        when(val result= compnayInfoResult.await()){
            is Resource.Success ->{
               state=  state.copy(
                    company = result.data,
                   isloading = false,
                   error = null
                )
            }
            is Resource.Error ->{
                state=  state.copy(
                    company = null,
                    isloading = false,
                    error = result.message
                )
            }
            else -> Unit
        }
        when(val result= intraDayInfo.await()){
            is Resource.Success ->{
                state=  state.copy(
                    StockInfos = result.data,
                    isloading = false,
                    error = null
                )
            }
            is Resource.Error ->{
                state=  state.copy(
                    StockInfos = null,
                    isloading = false,
                    error = result.message
                )
            }
            else -> Unit
        }
    }
}




}