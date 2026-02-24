package com.nicole.pokemonapp.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicole.domain.list.usecase.GetPokemonListUseCase
import com.nicole.pokemonapp.ui.pokemonlist.model.PokemonListUiState
import com.nicole.pokemonapp.ui.pokemonlist.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonListUseCase
) : ViewModel() {

    private val _allPokemon = MutableStateFlow(PokemonListUiState.DEFAULT)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    //private val _uiState = MutableStateFlow(PokemonListUiState.DEFAULT)
    val uiState: StateFlow<PokemonListUiState> = combine(
        _allPokemon,
        _searchQuery
    ) { allPokemon, searchQuery ->
        if (searchQuery.isBlank()) {
            PokemonListUiState(allPokemon.list)
        } else {
            val filteredPokemon = allPokemon.list.filter { pokemon ->
                pokemon.name.contains(searchQuery, ignoreCase = true)
            }
            PokemonListUiState(filteredPokemon)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PokemonListUiState.DEFAULT)

    init{
        viewModelScope.launch {
            println("PokemonListViewModel init")
            _allPokemon.value = getPokemonList().toUiState()
        }
    }

    fun onSearchQueryChanged(newQuery: String){
        _searchQuery.value = newQuery
    }

}