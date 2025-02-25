package com.nascenia.marketupdate.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


//fun CryptoDetailScreen(coinId: String, viewModel: CryptoViewModel) {
//    val cryptoList by viewModel.apiResponse.observeAsState()
//    val coin = cryptoList.find { it.id == coinId }
//
//    coin?.let {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            AsyncImage(
//                model = it.image,
//                contentDescription = "${it.name} icon",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//            )
//            Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
//            Text(text = "Symbol: ${it.symbol.uppercase()}")
//            Text(text = "Current Price: $${it.current_price}")
//            Text(text = "Market Cap: $${it.market_cap}")
//        }
//    } ?: Text(text = "Coin not found!", style = MaterialTheme.typography.headlineSmall)
//}