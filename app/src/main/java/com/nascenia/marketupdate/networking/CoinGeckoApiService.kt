package com.nascenia.marketupdate.networking

import com.nascenia.marketupdate.model.Coin
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {
    @GET("api/v3/coins/markets")
    suspend fun getMarketData(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids: String? = null
    ): Response<List<Coin>>
}