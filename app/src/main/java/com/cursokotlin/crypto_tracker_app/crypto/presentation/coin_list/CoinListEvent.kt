package com.cursokotlin.crypto_tracker_app.crypto.presentation.coin_list

import com.cursokotlin.crypto_tracker_app.crypto.domain.model.CryptoCoin


sealed interface CoinListEvent {
    data class OnSearchQueryChange(val query: String) : CoinListEvent
    data class OnCoinClick(val coin: CryptoCoin) : CoinListEvent
    data class OnToggleFavorite(val coin: CryptoCoin) : CoinListEvent
    data object OnRetryClick : CoinListEvent
}