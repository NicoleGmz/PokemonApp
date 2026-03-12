package com.nicole.pokemonapp.ui.pokemonlist.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.nicole.domain.list.model.PokemonItem
import com.nicole.pokemonapp.ui.pokemonlist.PokemonListViewModel
import com.nicole.pokemonapp.ui.pokemonlist.model.PokemonItemUiState
import com.nicole.pokemonapp.ui.theme.getPokemonTypeColor
import kotlinx.coroutines.launch

@Suppress("ParamsComparedByRef")
@Composable
fun PokemonListScreen(
    onPokemonClicked: (id: Int) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedTypes by viewModel.selectedTypes.collectAsStateWithLifecycle()
    val selectedGeneration by viewModel.selectedGeneration.collectAsStateWithLifecycle()

    val lazyPagingPokemonList = viewModel.pagedPokemonList.collectAsLazyPagingItems()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTopFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    var previousFirstId by remember { mutableStateOf<Any?>(null) }
    val currentFirstId = lazyPagingPokemonList.itemSnapshotList.items.firstOrNull()?.id


    LaunchedEffect(currentFirstId) {
        if (currentFirstId != null) {
            if (currentFirstId != previousFirstId && previousFirstId != null) {
                listState.scrollToItem(0)
            }
            previousFirstId = currentFirstId
        }else if (lazyPagingPokemonList.loadState.refresh is LoadState.NotLoading){
            previousFirstId = null
            //listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Center,
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTopFab,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ){
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ){
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
            }
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onSearchQueryChanged("") }
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
            )

            ExpandableFilterSelection(
                title = "Type",
                options = viewModel.allTypes,
                selectedOptions = selectedTypes,
                clearSelection = viewModel::clearTypeFilter,
                onOptionSelected = viewModel::toggleTypeFilter
            )

            HorizontalDivider( modifier = Modifier.padding(horizontal = 8.dp))

            ExpandableFilterSelection(
                title = "Generation",
                options = viewModel.allGenerations,
                selectedOptions = selectedGeneration,
                clearSelection = viewModel::clearGenerationFilter,
                onOptionSelected = viewModel::toggleGenerationFilter
            )

            HorizontalDivider( modifier = Modifier.padding(horizontal = 8.dp))

            PokemonList(listState, lazyPagingPokemonList, onPokemonClicked = onPokemonClicked)

            if (lazyPagingPokemonList.loadState.refresh is LoadState.Loading && lazyPagingPokemonList.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if(lazyPagingPokemonList.loadState.refresh is LoadState.NotLoading && lazyPagingPokemonList.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "No Pokemon Found")
                }
            }
        }
    }
}

@Composable
fun PokemonList(
    listState: LazyListState,
    pokemonList: LazyPagingItems<PokemonItemUiState>,
    onPokemonClicked: (id: Int) -> Unit
) {

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        items(
            count = pokemonList.itemCount,
            key = { index -> pokemonList[index]?.id ?: index }
        ) { index ->
            val pokemon = pokemonList[index]
            if (pokemon != null) {
                PokemonCard(pokemon, onPokemonClicked)
            }
        }

        if (pokemonList.loadState.append is LoadState.Loading) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Loading...")
                        Spacer(modifier = Modifier.size(16.dp))
                        CircularProgressIndicator()
                    }
                }
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


@Composable
fun ExpandableFilterSelection(
    title: String,
    options: List<String>,
    selectedOptions: Set<String> = emptySet(),
    clearSelection: () -> Unit = {},
    onOptionSelected: (String) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    val summary = if (selectedOptions.isEmpty()) "All" else selectedOptions.joinToString(", ")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "$title:", fontWeight = FontWeight.SemiBold)

                Text(
                    text = summary,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if(selectedOptions.isNotEmpty()){
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { clearSelection() }
                        .padding(end = 8.dp)
                    ,
                    tint = Color.Red
                )
            }

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        AnimatedVisibility(visible = expanded) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                //verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                options.forEach { option ->
                    val isSelected = option in selectedOptions
                    val chipColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent

                    FilterChip(
                        selected = isSelected,
                        onClick = { onOptionSelected(option) },
                        label = {
                            Text(text = option) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PokemonCardPreview(){
    val pokemonItem = PokemonItemUiState("Pikachu",
        25,
        "",
        listOf("Electric"),
        "Generation I",
        listOf(getPokemonTypeColor("Electric")),
        listOf(getPokemonTypeColor("Electric"), getPokemonTypeColor("Electric")))
    PokemonCard(pokemonItem, {})
}


@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview(){
    val mockData = listOf(
        PokemonItemUiState("Bulbasaur", 1, "", listOf("Grass"), "Generation I", listOf(getPokemonTypeColor("Grass"), getPokemonTypeColor("Poison")),
            listOf(getPokemonTypeColor("Grass"), getPokemonTypeColor("Poison"))),
        PokemonItemUiState("Charmander", 4, "", listOf("Fire"), "Generation I", listOf(getPokemonTypeColor("Fire")),
            listOf(getPokemonTypeColor("Fire"), getPokemonTypeColor("Fire"))),
        PokemonItemUiState("Pikachu", 25, "", listOf("Electric"),"Generation I", listOf(getPokemonTypeColor("Electric")),
            listOf(getPokemonTypeColor("Electric"), getPokemonTypeColor("Electric")))
    )

    //PokemonList(mockData,{})
}

@Preview(showBackground = true)
@Composable
fun ExpandableFilterSelectionPreview(){
    ExpandableFilterSelection(
        title = "Type",
        options = listOf("Grass", "Poison", "Electric", "Fire"),
        selectedOptions = setOf("Grass", "Poison"),
        onOptionSelected = {}
    )
}