package com.nicole.pokemonapp.ui.pokemonlist

import androidx.lifecycle.ViewModel
import com.nicole.domain.list.usecase.GetPokemonListUseCase
import com.nicole.pokemonapp.ui.pokemonlist.model.PokemonListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val useCase: GetPokemonListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.DEFAULT)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init{
        _uiState.value = PokemonListUiState(
            list = useCase()
        )
    }
}