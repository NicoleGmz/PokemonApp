package com.nicole.pokemonapp.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.nicole.pokemonapp.ui.pokemondetail.PokemonDetailViewModel
import com.nicole.pokemonapp.ui.pokemondetail.view.PokemonDetailScreen
import com.nicole.pokemonapp.ui.pokemonlist.view.PokemonListScreen
import com.nicole.pokemonapp.ui.welcome.WelcomeScreen

/**
 * The top-level Composable that hosts the application's navigation graph using Navigation 3.
 * Initial Route: Welcome
 */
@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Route.Welcome)

    NavDisplay(
        backStack = backStack,
        modifier = modifier.systemBarsPadding(),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {

            entry<Route.Welcome> {
                WelcomeScreen(
                    onContinueClicked = {
                        backStack.add(Route.PokemonList)
                    }
                )
            }

            entry<Route.PokemonList> {
                PokemonListScreen(
                    onPokemonClicked = { id ->
                        println("id: $id")
                        backStack.add(Route.PokemonDetail(id))
                    }
                )
            }

            entry<Route.PokemonDetail> { key ->
                val viewModel = hiltViewModel<PokemonDetailViewModel, PokemonDetailViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key.pokemonId)
                    }
                )
                PokemonDetailScreen(viewModel = viewModel)
            }
        },

        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally(tween(400)) { it } + fadeIn(tween(400)),
                initialContentExit = slideOutHorizontally(tween(400)) { -it / 4 } + fadeOut(
                    tween(
                        400
                    )
                )
            )
        },
        popTransitionSpec = {
            ContentTransform(
                targetContentEnter = slideInHorizontally(tween(400)) { -it / 4 } + fadeIn(tween(400)),
                initialContentExit = slideOutHorizontally(tween(400)) { it } + fadeOut(tween(400))
            )
        },
        //sceneStrategy = rememberListDetailSceneStrategy()
    )
}