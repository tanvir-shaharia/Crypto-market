package com.nascenia.marketupdate.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nascenia.marketupdate.model.Coin
import com.nascenia.marketupdate.networking.ApiResponse
import com.nascenia.marketupdate.networking.CoinGeckoApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CryptoViewModel(private val api: CoinGeckoApiService) : ViewModel() {

    private val _apiResponse = MutableLiveData<ApiResponse<List<Coin>>>()
    val apiResponse: LiveData<ApiResponse<List<Coin>>> get() = _apiResponse

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing

    // Function to fetch market data
    fun fetchMarketData() {
        _isRefreshing.value = true // Set refreshing state to true when data fetching starts

        viewModelScope.launch {
            _apiResponse.postValue(ApiResponse.Loading)

            try {
                val response = api.getMarketData()
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        _apiResponse.postValue(ApiResponse.Success(data))
                    } ?: run {
                        _apiResponse.postValue(ApiResponse.Error("Empty response body"))
                        Log.e("CryptoViewModel", "Empty response body")
                    }
                } else {
                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
                    _apiResponse.postValue(ApiResponse.Error(errorMessage))
                    Log.e("CryptoViewModel", errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Exception: ${e.message ?: "Unknown error"}"
                _apiResponse.postValue(ApiResponse.Error(errorMessage))
                Log.e("CryptoViewModel", errorMessage)
            } finally {
                _isRefreshing.value = false // Ensure refreshing state is false once done (error or success)
            }
        }
    }

    // Refresh data (trigger fetching again)
    fun refreshCryptoData() {
        _isRefreshing.value = true // Indicate refreshing state
        fetchMarketData()
    }
}


