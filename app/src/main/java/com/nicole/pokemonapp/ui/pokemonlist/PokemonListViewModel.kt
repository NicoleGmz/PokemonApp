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
    private val _selectedTypes = MutableStateFlow(emptySet<String>())

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val selectedTypes: StateFlow<Set<String>> = _selectedTypes.asStateFlow()

    //private val _uiState = MutableStateFlow(PokemonListUiState.DEFAULT)
    val uiState: StateFlow<PokemonListUiState> = combine(
        _allPokemon,
        _searchQuery,
        _selectedTypes
    ) { allPokemon, query, types ->
        if (query.isBlank() && types.isEmpty()) {
            PokemonListUiState(allPokemon.list)
        } else {
            /*val filteredPokemon = allPokemon.list.filter { pokemon ->
                pokemon.name.contains(searchQuery, ignoreCase = true)
                        && (!selectedTypes.isEmpty() || pokemon.types.any { it in selectedTypes })
            }*/

            var filteredPokemon = allPokemon.list

            if(query.isNotBlank()){
                filteredPokemon = filteredPokemon.filter { pokemon ->
                    pokemon.name.contains(query, ignoreCase = true)
                    pokemon.id.toString().contains(query)
                }
            }

            if(types.isNotEmpty()){
                filteredPokemon = filteredPokemon.filter { pokemon ->
                    pokemon.types.any { it in types }
                }
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

    fun toggleTypeFilter(type: String) {
        _selectedTypes.value = _selectedTypes.value.toMutableSet().apply {
            if (contains(type)) remove(type) else add(type)
        }
    }

}