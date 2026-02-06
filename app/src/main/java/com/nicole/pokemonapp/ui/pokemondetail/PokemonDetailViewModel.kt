package com.nicole.pokemonapp.ui.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicole.domain.detail.usecase.GetPokemonDetailUseCase
import com.nicole.pokemonapp.ui.pokemondetail.model.PokemonDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetail: GetPokemonDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.DEFAULT)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            //_uiState.value = getPokemonDetail()
        }
    }
}