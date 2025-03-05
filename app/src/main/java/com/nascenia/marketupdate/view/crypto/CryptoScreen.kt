package com.nascenia.marketupdate.view.crypto

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nascenia.marketupdate.model.Coin
import com.nascenia.marketupdate.networking.ApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListScreen(
    viewModel: CryptoViewModel,
    onItemClick: (Coin) -> Unit
) {
    // Observe the API response
    val apiResponse by viewModel.apiResponse.observeAsState(ApiResponse.Loading)

    // Trigger fetching when the screen is launched
    LaunchedEffect(Unit) {
        viewModel.fetchMarketData()
    }

    // Observe the refresh state
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    // Wrapping content in PullToRefreshBox
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refreshCryptoData() // Trigger a refresh on pull-to-refresh
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (apiResponse) {
                is ApiResponse.Loading -> {
                    ShimmerLoadingList(modifier = Modifier.fillMaxSize())
                }

                is ApiResponse.Success -> {
                    CryptoList(
                        cryptoList = (apiResponse as ApiResponse.Success).data,
                        modifier = Modifier.fillMaxSize(),
                        onItemClick = onItemClick
                    )
                }

                is ApiResponse.Error -> {
                    // Display error screen and stop the refreshing icon
                    ErrorState(
                        errorMessage = (apiResponse as ApiResponse.Error).message,
                        onRetry = { viewModel.refreshCryptoData() } // Allow retry on error
                    )
                    // Ensure the refresh icon stops even when error happens
                    if (isRefreshing) {
                        viewModel.refreshCryptoData() // Ensure refreshing stops on error
                    }
                }
            }
        }
    }
}

@Composable
fun CryptoList(
    cryptoList: List<Coin>,
    modifier: Modifier = Modifier,
    onItemClick: (Coin) -> Unit
) {
    if (cryptoList.isEmpty()) {
        EmptyState(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cryptoList) { crypto ->
                CryptoItem(crypto = crypto, onItemClick = onItemClick)
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CryptoItem(
    crypto: Coin,
    onItemClick: (Coin) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onItemClick(crypto)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = crypto.image,
            contentDescription = "${crypto.symbol} icon",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = crypto.symbol.uppercase(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "$${String.format("%.2f", crypto.current_price)}",
                color = Color.Green,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $errorMessage",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tap to retry",
            modifier = Modifier
                .clickable { onRetry() } // Retry action
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No crypto data available",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ShimmerLoadingList(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(15) {
            ShimmerCryptoItem()
        }
    }
}

@Composable
fun ShimmerCryptoItem() {
    val shimmerBrush = rememberShimmerBrush()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(shimmerBrush)
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmerBrush)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmerBrush)
            )
        }
    }
}

@Composable
fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f), // Base color
        Color.LightGray.copy(alpha = 0.3f), // Highlight
        Color.LightGray.copy(alpha = 0.6f)  // Back to base
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 1000f, 0f)
    )
}