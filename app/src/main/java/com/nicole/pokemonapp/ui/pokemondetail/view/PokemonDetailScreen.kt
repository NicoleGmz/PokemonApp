package com.nicole.pokemonapp.ui.pokemondetail.view

import androidx.annotation.Px
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.pokemonapp.ui.pokemondetail.PokemonDetailViewModel

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    // Screen content
    PokemonDetailComponent(pokemonDetail = uiState.value.pokemonDetail)
}

@Composable
fun PokemonDetailComponent(
    pokemonDetail: PokemonDetail
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = pokemonDetail.name,
            fontSize = 32.sp,
            maxLines = 1,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        AsyncImage(
            model = pokemonDetail.sprite,
            contentDescription = pokemonDetail.name,
            modifier = Modifier.size(128.dp).weight(1f).padding(8.dp),
            error = painterResource(id = android.R.drawable.stat_notify_error),
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "ID: ", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(text = pokemonDetail.id.toString(), fontSize = 24.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Height: ", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(text = pokemonDetail.height.toString(), fontSize = 24.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(text = "Weight: ", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(text = pokemonDetail.weight.toString(), fontSize = 24.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(text = "Types: ", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(text = pokemonDetail.types.joinToString(", "), fontSize = 24.sp)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailComponentPreview(){
    PokemonDetailComponent(
        pokemonDetail = PokemonDetail(
            id = 1,
            name = "Pikachu",
            height = 50,
            weight = 100,
            sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$1.png",
            types = listOf("Electric")
        )
    )
}