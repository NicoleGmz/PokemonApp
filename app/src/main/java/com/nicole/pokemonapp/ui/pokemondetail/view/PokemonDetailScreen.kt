package com.nicole.pokemonapp.ui.pokemondetail.view

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.nicole.pokemonapp.ui.pokemondetail.PokemonDetailViewModel
import com.nicole.pokemonapp.ui.pokemondetail.model.PokemonDetailUiState
import com.nicole.pokemonapp.ui.theme.getPokemonTypeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val pokemonDetail = uiState.value

    if (pokemonDetail == PokemonDetailUiState.DEFAULT){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column() {
                Text(text = "Loading...")
                Spacer(modifier = Modifier.size(16.dp))
                CircularProgressIndicator()
            }
        }
    }else {
        PokemonDetailScaffold(pokemonDetail = pokemonDetail, onBackClicked = onBackClicked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScaffold(
    pokemonDetail: PokemonDetailUiState,
    onBackClicked: () -> Unit
){
    val mainType = pokemonDetail.types.firstOrNull() ?: "normal"
    val typeColor = pokemonDetail.types.map { getPokemonTypeColor(it) }
    val gradientColor = if (typeColor.size == 1){
        listOf(typeColor.first(), typeColor.first())
    }else{
        typeColor
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientColor[0]),
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize().background(brush = Brush.verticalGradient(gradientColor)),
            contentAlignment = Alignment.TopStart
        ) {

            PokemonDetailComponent(pokemonDetail = pokemonDetail)

        }
    }
}


@Composable
fun PokemonDetailComponent(
    pokemonDetail: PokemonDetailUiState
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

            PokemonStatsComponent(pokemonDetail.stats)
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

@Composable
fun PokemonStatsComponent(
    pokemonStat: List<List<String>>,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
    ){
        Column() {
            Text("Basic Stats: ")

            Spacer(modifier = Modifier.size(8.dp))

            for (stat in pokemonStat) {
                PokemonStatBar(stat[0], stat[1].toInt(), stat[2].toFloat() )
            }
        }
    }

}



@Composable
fun PokemonStatBar(
    statName: String,
    statValue: Int,
    percentageValue: Float
){
    val statColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = statName)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = statValue.toString())
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .weight(0.55f)
                .height(10.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.5f))
        ) {
            // 4. The Foreground Bar (Filled Track)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentageValue) // Width is controlled by the animation
                    .clip(CircleShape)
                    .background(statColor)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PokemonStatBarPreview() {
    PokemonStatBar(
        "HP",
        100,
        255.0f
    )
}


@Preview(showBackground = true)
@Composable
fun PokemonDetailComponentPreview() {
    PokemonDetailComponent(
        pokemonDetail = PokemonDetailUiState(
            id = 1,
            name = "Pikachu",
            height = 50,
            weight = 100,
            sprite = "",
            types = listOf("Electric"),
            generation = "",
            stats = listOf(
                listOf("HP", "100", "255"),
            )
        )
    )
}