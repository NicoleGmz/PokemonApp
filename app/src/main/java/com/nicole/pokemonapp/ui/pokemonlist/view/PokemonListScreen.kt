package com.nicole.pokemonapp.ui.pokemonlist.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicole.domain.list.model.PokemonItem
import com.nicole.pokemonapp.ui.pokemonlist.PokemonListViewModel

@Suppress("ParamsComparedByRef")
@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    // Screen content
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    PokemonList(pokemonList = uiState.value.list)
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonItem>
){
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(8.dp),    ){
        items(pokemonList){
            PokemonCard(pokemon = it)
        }
    }
}

@Composable
fun PokemonCard(pokemon: PokemonItem) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = pokemon.name)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = pokemon.id.toString())
        }
    }
}

@Preview
@Composable
fun PokemonCardPreview(){
    val pokemonItem = PokemonItem("Pikachu", 25)
    PokemonCard(pokemonItem)
}


@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview(){
    val mockData = listOf(
        PokemonItem("Pikachu", 25),
        PokemonItem("Bulbasaur", 1),
        PokemonItem("Charmander", 4)
    )
    PokemonList(mockData)
}