package com.nicole.pokemonapp.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nicole.domain.list.usecase.GetPagePokemonListUseCase
import com.nicole.domain.list.usecase.GetPokemonListUseCase
import com.nicole.pokemonapp.ui.pokemonlist.model.PokemonItemUiState
import com.nicole.pokemonapp.ui.pokemonlist.model.PokemonListUiState
import com.nicole.pokemonapp.ui.pokemonlist.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonList: GetPokemonListUseCase,
    private val getPagedPokemonList: GetPagePokemonListUseCase
) : ViewModel() {

    private val _allPokemon = MutableStateFlow(PokemonListUiState.DEFAULT)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTypes = MutableStateFlow(emptySet<String>())

    val selectedTypes: StateFlow<Set<String>> = _selectedTypes.asStateFlow()

    private val _selectedGeneration = MutableStateFlow(emptySet<String>())
    val selectedGeneration: StateFlow<Set<String>> = _selectedGeneration.asStateFlow()

    //private val _uiState = MutableStateFlow(PokemonListUiState.DEFAULT)
    val uiState: StateFlow<PokemonListUiState> = combine(
        _allPokemon,
        _searchQuery,
        _selectedTypes,
        _selectedGeneration
    ) { allPokemon, query, types, generations ->
        /*if (query.isBlank() && types.isEmpty() && generations.isEmpty()) {
            PokemonListUiState(allPokemon.list)
        } else {*/
            var filteredPokemon = allPokemon.list

            if(query.isNotBlank()){
                filteredPokemon = filteredPokemon.filter { pokemon ->
                    pokemon.name.contains(query, ignoreCase = true) ||
                    pokemon.id.toString().contains(query)
                }
            }

            if(types.isNotEmpty()){
                filteredPokemon = filteredPokemon.filter { pokemon ->
                    types.all { selectedType ->
                        pokemon.types.any { it == selectedType }
                    }
                   // pokemon.types.all { it in types }
                }
            }

            if(generations.isNotEmpty()){
                filteredPokemon = filteredPokemon.filter { pokemon ->
                    pokemon.generation in generations
                }
            }

            PokemonListUiState(filteredPokemon)
        //}
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PokemonListUiState.DEFAULT
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedPokemonList: Flow<PagingData<PokemonItemUiState>> = combine(
        _searchQuery,
        _selectedTypes,
        _selectedGeneration
    ) { query, types, generations ->
        Triple(query, types, generations)
    }.flatMapLatest { (query, types, generations) ->
        getPagedPokemonList(
            searchQuery = query,
            typeFilter = types,
            generationFilter = generations
        ).map { pagingData ->
            pagingData.map { pokemonItem ->
                pokemonItem.toUiState()
            }
        }.cachedIn(viewModelScope)
    }

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

    fun toggleGenerationFilter(generation: String) {
        _selectedGeneration.value = _selectedGeneration.value.toMutableSet().apply {
            if (contains(generation)) remove(generation) else add(generation)
        }
    }

    fun clearTypeFilter(){
        _selectedTypes.value = emptySet()
    }

    fun clearGenerationFilter(){
        _selectedGeneration.value = emptySet()
    }

}