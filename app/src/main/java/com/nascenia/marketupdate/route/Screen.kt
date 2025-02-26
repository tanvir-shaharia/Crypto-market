package com.nascenia.marketupdate.route

sealed class Screen(val route: String) {
    object CryptoList : Screen("crypto_list")
    data class CryptoDetail(val coinJson: String) : Screen("crypto_detail/{coinJson}") {
        fun createRoute() = "crypto_detail/$coinJson"
    }
}