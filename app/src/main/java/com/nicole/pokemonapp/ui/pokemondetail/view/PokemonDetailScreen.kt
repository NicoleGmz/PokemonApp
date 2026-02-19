package com.nicole.pokemonapp.ui.pokemondetail.view

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.nicole.domain.detail.model.PokemonDetail
import com.nicole.pokemonapp.ui.pokemondetail.PokemonDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val pokemonDetail = uiState.value.pokemonDetail
    // Screen content
    //PokemonDetailComponent(pokemonDetail = uiState.value.pokemonDetail, onBackClicked = onBackClicked)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pokemon Detail",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            if (pokemonDetail == PokemonDetail.DEFAULT) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }else{
                PokemonDetailComponent(pokemonDetail = pokemonDetail)
            }
        }
    }
}

@Composable
fun PokemonDetailComponent(
    pokemonDetail: PokemonDetail
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = pokemonDetail.name,
            fontSize = 32.sp,
            maxLines = 1,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )


        AsyncImage(
            model = pokemonDetail.sprite,
            contentDescription = pokemonDetail.name,
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp),
            error = painterResource(id = R.drawable.stat_notify_error),
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailRow("ID", pokemonDetail.id.toString())
            DetailRow("Height", pokemonDetail.height.toString())
            DetailRow("Weight", pokemonDetail.weight.toString())
            DetailRow("Generation", pokemonDetail.generation)
            DetailRow("Types", pokemonDetail.types.joinToString(", "))
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // Pushes Label to left, Value to right
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PokemonDetailComponentPreview() {
    PokemonDetailComponent(
        pokemonDetail = PokemonDetail(
            id = 1,
            name = "Pikachu",
            height = 50,
            weight = 100,
            sprite = "",
            types = listOf("Electric"),
            generation = ""
        )
    )
}