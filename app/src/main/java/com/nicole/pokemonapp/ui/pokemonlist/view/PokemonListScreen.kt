package com.nicole.pokemonapp.ui.pokemonlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.nicole.domain.list.model.PokemonItem
import com.nicole.pokemonapp.ui.pokemonlist.PokemonListViewModel
import com.nicole.pokemonapp.ui.pokemonlist.model.PokemonItemUiState
import com.nicole.pokemonapp.ui.theme.getPokemonTypeColor

@Suppress("ParamsComparedByRef")
@Composable
fun PokemonListScreen(
    onPokemonClicked: (id: Int) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery = viewModel.searchQuery.collectAsState()

    Scaffold{ paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
            )
            PokemonList(pokemonList = uiState.value.list, onPokemonClicked = onPokemonClicked)
        }

    }

}

@Composable
fun PokemonList(
    pokemonList: List<PokemonItemUiState>,
    onPokemonClicked: (id: Int) -> Unit
){
    if (pokemonList.isEmpty()){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Loading...")
                Spacer(modifier = Modifier.size(16.dp))
                CircularProgressIndicator()
            }
        }
    }else{
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(8.dp),    ){
            items(pokemonList){
                PokemonCard(pokemon = it, onClick = onPokemonClicked)
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: PokemonItemUiState, onClick: (id: Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.Transparent),
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth(),
        onClick = { onClick(pokemon.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.horizontalGradient(pokemon.gradientColor))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = pokemon.id.toString())
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = pokemon.name)
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                model = pokemon.sprite,
                contentDescription = pokemon.name,
                modifier = Modifier.size(48.dp),
                error = rememberVectorPainter(image = Icons.Outlined.Error),
            )
        }
    }
}

@Preview
@Composable
fun PokemonCardPreview(){
    val pokemonItem = PokemonItemUiState("Pikachu",
        25,
        "",
        listOf("Electric"),
        "",
        listOf(getPokemonTypeColor("Electric")),
        listOf(getPokemonTypeColor("Electric"), getPokemonTypeColor("Electric")))
    PokemonCard(pokemonItem, {})
}


@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview(){
    val mockData = listOf(
        PokemonItemUiState("Bulbasaur", 1, "", listOf("Grass"), "", listOf(getPokemonTypeColor("Grass"), getPokemonTypeColor("Poison")),
            listOf(getPokemonTypeColor("Grass"), getPokemonTypeColor("Poison"))),
        PokemonItemUiState("Charmander", 4, "", listOf("Fire"), "", listOf(getPokemonTypeColor("Fire")),
            listOf(getPokemonTypeColor("Fire"), getPokemonTypeColor("Fire"))),
        PokemonItemUiState("Pikachu", 25, "", listOf("Electric"),"", listOf(getPokemonTypeColor("Electric")),
            listOf(getPokemonTypeColor("Electric"), getPokemonTypeColor("Electric")))
    )

    PokemonList(mockData,{})
}