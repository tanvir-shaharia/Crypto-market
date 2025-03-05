package com.nascenia.marketupdate.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun CryptoDetailScreen(coinId: String?, viewModel: CryptoViewModel) {
    val coin = remember(coinId) { viewModel.getCoinById(coinId ?: "") }

    if (coin == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Coin not found", fontSize = 20.sp, color = Color.Red)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = coin.image,
                    contentDescription = "${coin.symbol} icon",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(text = coin.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Symbol: ${coin.symbol.uppercase()}", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailRow(label = "Current Price", value = "$${coin.current_price}")
                    DetailRow(label = "24H High", value = "$${coin.high_24h}")
                    DetailRow(label = "24H Low", value = "$${coin.low_24h}")
                    DetailRow(label = "Market Cap", value = "$${coin.market_cap}")
                    DetailRow(label = "24H Market Cap Change", value = "${coin.market_cap_change_percentage_24h}%")
                    DetailRow(label = "All-Time High (ATH)", value = "$${coin.ath} (${coin.ath_change_percentage}%)")
                    DetailRow(label = "ATH Date", value = formatDate(coin.ath_date))
                    DetailRow(label = "All-Time Low (ATL)", value = "$${coin.atl} (${coin.atl_change_percentage}%)")
                    DetailRow(label = "ATL Date", value = formatDate(coin.atl_date))
                    DetailRow(label = "Total Supply", value = "${coin.total_supply}")
                    DetailRow(label = "Circulating Supply", value = "${coin.circulating_supply}")
                    DetailRow(label = "Max Supply", value = "${coin.max_supply}")
                    DetailRow(label = "Last Updated", value = formatDate(coin.last_updated))
                }
            }
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        val dateTime = LocalDateTime.parse(dateString, inputFormatter)
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        "N/A"
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}



