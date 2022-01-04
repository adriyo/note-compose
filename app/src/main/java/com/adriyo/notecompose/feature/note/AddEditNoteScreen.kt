package com.adriyo.notecompose.feature.note

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adriyo.notecompose.shared.components.TransparentTextField
import kotlinx.coroutines.flow.collectLatest


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    AnimatedVisibility(
        visible = true,
        enter = expandVertically() + slideInVertically(),
        exit = slideOutVertically()
    ) {
        AddEditNoteScreenContent(navController = navController, viewModel = viewModel)
    }
}

@ExperimentalComposeUiApi
@Composable
fun AddEditNoteScreenContent(navController: NavController, viewModel: AddEditNoteViewModel) {
    val titleState = viewModel.noteTitle.value
    val noteState = viewModel.noteContent.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveNote()
                    }) {
                        Icon(Icons.Default.Check, null)
                    }
                }
            )
        },
        backgroundColor = MaterialTheme.colors.background,
    ) {
        FormInput(titleState.text, noteState.text,
            onTitleChange = {
                viewModel.onTitleValueChange(it)
            },
            onNoteChange = {
                viewModel.onNoteContentValueChange(it)
            })
    }
}

@ExperimentalComposeUiApi
@Composable
fun FormInput(
    title: String = "",
    note: String = "",
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier
    ) {
        TransparentTextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = modifier,
            hintTitle = "Title",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            textStyle = MaterialTheme.typography.h6,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequester.requestFocus()
                }
            )
        )
        TransparentTextField(
            modifier = modifier
                .fillMaxHeight()
                .focusRequester(focusRequester),
            value = note,
            onValueChange = onNoteChange,
            hintTitle = "Note",
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
    }
}
