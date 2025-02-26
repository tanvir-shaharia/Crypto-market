package com.nascenia.marketupdate.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import com.nascenia.marketupdate.model.Coin


@Composable
fun CryptoDetailScreen(navBackStackEntry: NavBackStackEntry) {
    // Retrieve the coinJson string from the navigation arguments
    val coinJson = navBackStackEntry.arguments?.getString("coinJson") ?: ""

    // Deserialize the JSON string back into a Coin object
    val coin = Gson().fromJson(coinJson, Coin::class.java)

    // Display the Coin's details
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Coin Detail", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Display Coin data
        Text(text = "Coin ID: ${coin.id}")
        Text(text = "Name: ${coin.name}")
        Text(text = "Symbol: ${coin.symbol}")
        Text(text = "Price: $${coin.current_price}")
    }
}
