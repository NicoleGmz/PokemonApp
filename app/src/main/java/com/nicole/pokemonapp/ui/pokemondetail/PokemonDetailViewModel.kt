package com.nicole.pokemonapp.ui.pokemondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nicole.domain.detail.usecase.GetPokemonDetailUseCase
import com.nicole.pokemonapp.navigation.Route
import com.nicole.pokemonapp.ui.pokemondetail.model.PokemonDetailUiState
import com.nicole.pokemonapp.ui.pokemondetail.model.toUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = PokemonDetailViewModel.Factory::class)
class PokemonDetailViewModel @AssistedInject constructor(
    @Assisted val id: Int,
    private val getPokemonDetail: GetPokemonDetailUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory{
        fun create(id: Int): PokemonDetailViewModel
    }
    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.DEFAULT)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = getPokemonDetail(id).toUiState()
        }
    }
}