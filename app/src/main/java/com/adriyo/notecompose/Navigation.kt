package com.adriyo.notecompose

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.adriyo.notecompose.feature.note.AddEditNoteScreen
import com.adriyo.notecompose.feature.note.NotesListScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.NoteListScreen.route
    ) {
        composable(route = Screen.NoteListScreen.route) {
            NotesListScreen(navController = navController)
        }
        composable(
            route = Screen.AddEditNoteScreen.route + "?noteId={noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.NoteListScreen.route -> scaleIn() + expandHorizontally(
                        expandFrom = Alignment.Start
                    ) + fadeIn(
                        initialAlpha = 0.3f
                    )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.NoteListScreen.route -> scaleOut() + shrinkHorizontally(
                        shrinkTowards = Alignment.Start
                    ) + fadeOut()
                    else -> null
                }
            },
        ) {
            AddEditNoteScreen(
                navController = navController,
            )
        }
    }
}