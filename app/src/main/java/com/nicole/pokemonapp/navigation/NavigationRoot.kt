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
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

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

            // --- Initial Screen ---
            entry<Route.Welcome> {
                // TODO: Add your Welcome Composable here
            }

            /* // Example for List-Detail pattern:
            entry<Route.ExampleList>(
                metadata = ListDetailSceneStrategy.listPane()
            ) { 
                // ListScreen(onItemClick = { backStack.add(Route.ExampleDetail(it)) })
            }
            */
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