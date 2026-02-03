package com.nicole.pokemonapp.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicole.domain.list.usecase.GetPokemonListUseCase
import com.nicole.pokemonapp.ui.pokemonlist.model.PokemonListUiState
import com.nicole.pokemonapp.ui.pokemonlist.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.DEFAULT)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch {
            _uiState.value = getPokemonList().toUiState()
        }
    }

}