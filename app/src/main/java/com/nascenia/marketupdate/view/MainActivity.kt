package com.nascenia.marketupdate.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nascenia.marketupdate.networking.RetrofitInstance
import com.nascenia.marketupdate.ui.theme.MarketUpdateTheme
import com.nascenia.marketupdate.view.crypto.CryptoListScreen
import com.nascenia.marketupdate.view.crypto.CryptoViewModel
import com.nascenia.marketupdate.view.details.CryptoDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private lateinit var cryptoViewModel: CryptoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        cryptoViewModel = CryptoViewModel(RetrofitInstance.api)


        splashScreen.setKeepOnScreenCondition { false }

        setContent {
            MarketUpdateTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Crypto Market",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                titleContentColor = Color.White
                            )
                        )
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController, startDestination = "crypto_list") {
                            composable("crypto_list") {
                                CryptoListScreen(viewModel = cryptoViewModel, onItemClick = { coin ->
                                    navController.navigate("crypto_detail/${coin.id}")
                                })
                            }
                            composable(
                                "crypto_detail/{coinId}",
                                arguments = listOf(navArgument("coinId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val coinId = backStackEntry.arguments?.getString("coinId")
                                if (coinId != null) {
                                    CryptoDetailScreen(coinId = coinId, viewModel = cryptoViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}