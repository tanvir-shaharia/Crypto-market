package com.nascenia.marketupdate.route

sealed class Screen(val route: String) {
    data object CryptoList : Screen("crypto_list")
    data object CryptoDetail : Screen("crypto_detail/{coinId}") {
        fun createRoute(coinId: String) = "crypto_detail/$coinId"
    }
}