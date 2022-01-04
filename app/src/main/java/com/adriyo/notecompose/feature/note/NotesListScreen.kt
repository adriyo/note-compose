package com.adriyo.notecompose.feature.note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.adriyo.notecompose.Screen
import com.adriyo.notecompose.data.model.Note
import com.adriyo.notecompose.shared.components.StaggeredVerticalGrid

enum class ListShowType {
    Grid, List
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun AnimatedVisibilityScope.NotesListScreen(
    navController: NavHostController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val showMenu = remember { mutableStateOf(false) }
    val showTypeList = remember { mutableStateOf(ListShowType.Grid) }
    val noteListState = viewModel.notesState.collectAsState()
    val selectedItems = viewModel.selectedNotesState.collectAsState().value
    BackHandler(enabled = selectedItems.isNotEmpty()) {
        viewModel.resetSelectedItems()
    }

    Scaffold(
        topBar = {
            NoteListScreenAppBar(showTypeList, selectedItems, viewModel)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.resetSelectedItems()
                navController.navigate(Screen.AddEditNoteScreen.route)
            }, backgroundColor = MaterialTheme.colors.secondary) {
                Icon(Icons.Default.Add, "open view for add task")
            }
        },
        backgroundColor = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.padding(vertical = 8.dp)) {
            if (noteListState.value.isNullOrEmpty()) {
                EmptyScreen()
            } else {
                NotesListView(noteListState.value, showTypeList.value, NoteListItemState(
                    onClick = { note ->
                        if (selectedItems.isNotEmpty()) {
                            viewModel.updateSelectedItem(note)
                        } else {
                            navController.navigate(
                                Screen.AddEditNoteScreen.route.plus("?noteId=${note.localId}")
                            )
                        }
                    },
                    onLongClick = { note ->
                        viewModel.updateSelectedItem(note)
                    }
                ))
            }
        }

    }
}

@ExperimentalAnimationApi
@Composable
fun NoteListScreenAppBar(
    showTypeList: MutableState<ListShowType>,
    selectedItems: List<Boolean>,
    viewModel: NotesViewModel
) {
    TopAppBar(
        title = {
            Text(text = "To-Do App")
        },
        actions = {
            IconButton(onClick = {
                if (showTypeList.value == ListShowType.List) {
                    showTypeList.value = ListShowType.Grid
                } else {
                    showTypeList.value = ListShowType.List
                }
            }) {
                if (showTypeList.value == ListShowType.Grid) {
                    Icon(Icons.Default.ListAlt, null)
                } else {
                    Icon(Icons.Default.GridView, null)
                }
            }
            // TODO enable this after implement retrofit
            /*IconButton(onClick = { showMenu.value = !showMenu.value }) {
                Icon(Icons.Default.MoreVert, null)
            }
            DropdownMenu(expanded = showMenu.value,
                onDismissRequest = { showMenu.value = false }) {
                DropdownMenuItem(onClick = {
                    showMenu.value = false
                }) {
                    Text(
                        text = "Mark all as completed",
                        style = MaterialTheme.typography.caption
                    )
                }
                DropdownMenuItem(onClick = {
                    showMenu.value = false
                }) {
                    Text(
                        text = "Sync with Remote DB",
                        style = MaterialTheme.typography.caption
                    )
                }
            }*/
        }
    )

    AnimatedVisibility(
        visible = selectedItems.isNotEmpty(),
        enter = slideInVertically() + expandHorizontally(
            expandFrom = Alignment.Start
        ) + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkHorizontally() + fadeOut()
    ) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            navigationIcon = {
                IconButton(onClick = { viewModel.resetSelectedItems() }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            },
            title = {
                Text("${selectedItems.size}")
            },
            actions = {
                IconButton(onClick = { viewModel.deleteSelectedItems() }) {
                    Icon(Icons.Default.Delete, null)
                }
            },
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun AnimatedVisibilityScope.NotesListView(
    notes: List<Note>,
    typeView: ListShowType,
    noteState: NoteListItemState
) {
    if (typeView == ListShowType.Grid) {
        StaggeredVerticalGrid(
            maxColumnWidth = 220.dp,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            notes.forEach { note ->
                NoteItemView(note = note, noteState)
            }
        }
    } else if (typeView == ListShowType.List) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            items(notes) { note ->
                NoteItemView(note = note, noteState)
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun AnimatedVisibilityScope.NoteItemView(note: Note, noteState: NoteListItemState) {
    val borderWidth = if (note.isSelected) 3.dp else 1.dp
    val borderColor = if (note.isSelected) MaterialTheme.colors.primary else Color.LightGray
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(5.dp)
            )
            .combinedClickable(
                onClick = {
                    noteState.onClick(note)
                },
                onLongClick = {
                    noteState.onLongClick(note)
                }
            )
            .animateEnterExit()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Spacer(Modifier.width(12.dp))
            Column {
                if (!note.title.isEmpty()) {
                    Text(text = note.title, style = MaterialTheme.typography.subtitle2)
                }
                if (!note.note.isEmpty()) {
                    Text(
                        text = note.note, style = MaterialTheme.typography.body1.copy(
                            fontSize = 12.sp
                        ), maxLines = 8
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Notes, null,
            modifier = Modifier
                .width(96.dp)
                .height(96.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(text = "Notes you add appear here", style = MaterialTheme.typography.caption)
    }
}

data class NoteListItemState(
    val onClick: (Note) -> Unit,
    val onLongClick: (Note) -> Unit
)