package org.example.mytask.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.mytask.WindowWidthSize
import org.example.mytask.core.ObserveAsEvents
import org.example.mytask.core.SnackBarController
import org.example.mytask.presentation.components.AddTaskModal
import org.example.mytask.presentation.components.NavigationList
import org.example.mytask.presentation.components.RouteNames
import org.example.mytask.presentation.components.getTitle
import org.example.mytask.presentation.screens.task_details.TaskDetailsScreen
import org.example.mytask.presentation.screens.tasks.TaskActions
import org.example.mytask.presentation.screens.tasks.TaskUiState
import org.example.mytask.presentation.screens.tasks.TaskViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BaseApp(widthSizeClass: WindowWidthSize, modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }
    ObserveAsEvents(flow = SnackBarController.events, snackBarHostState) { event ->
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            val result = snackBarHostState.showSnackbar(
                event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }
    val taskViewModel: TaskViewModel = koinViewModel()
    val taskUiState: TaskUiState by taskViewModel.uiState.collectAsState()

    val taskToEdit by taskViewModel.taskToEdit


    //My add task component
    AddTaskModal(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        toEdit = false,
        onTitleChange = taskViewModel::onTitleChange,
        onDescriptionChange = taskViewModel::onDescriptionChange,
        task = taskToEdit,
        taskUiState = taskUiState,
        currentDestination = currentDestination,
        onSubmit = { task, action ->
            taskViewModel.onTaskAction(action, task, callback = {
                showDialog =
                    false
            })
        },
        modifier = Modifier.fillMaxSize()
    )

    //main base responsive contain
    if (widthSizeClass in listOf(
            WindowWidthSize.Compact,
            widthSizeClass == WindowWidthSize.Medium
        )
    ) {
        CompactAndMediumScreen(
            navHostController = navHostController,
            snackBarHostState = snackBarHostState,
            currentDestination = currentDestination,
            taskViewModel = taskViewModel,
            taskUiState = taskUiState,
            onNavigationRouteNameChange = { route ->
                scope.launch { drawerState.close() }
                navHostController.navigate(route)
                taskViewModel.setSelectedTask("")
            },
            onShowDialog = { showDialog = it },
            drawerState = drawerState,
            scope = scope
        )
    } else {
        ExpandedScreen(
            navHostController = navHostController,
            snackBarHostState = snackBarHostState,
            currentDestination = currentDestination,
            taskViewModel = taskViewModel,
            taskUiState = taskUiState,
            onNavigationRouteNameChange = { route ->
                navHostController.navigate(route)
                taskViewModel.setSelectedTask("")
            },
            onShowDialog = { showDialog = it },
            scope = scope
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactAndMediumScreen(
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState,
    currentDestination: NavDestination?,
    taskViewModel: TaskViewModel,
    taskUiState: TaskUiState,
    onNavigationRouteNameChange: (String) -> Unit,
    onShowDialog: (Boolean) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxHeight(),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                Column {
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                modifier = Modifier.padding(16.dp).size(24.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        taskViewModel.authUser.value.email.ifEmpty { "Anonymous" },
                        Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(12.dp))
                    NavigationList(currentDestination?.route, onNavigationRouteNameChange)
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                TopAppBar(
                    title = {
                        Text(
                            getTitle(currentDestination),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        if (currentDestination?.route?.contains(RouteNames.TasksDetails.route) == false) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    "Show menu",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        } else {
                            IconButton(onClick = {
                                navHostController.navigateUp()
                                //taskViewModel.updateCurrentTask(null)
                            }) {
                                Icon(
                                    Icons.Rounded.ArrowBack,
                                    "Back",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    })
            },
            floatingActionButton = {
                if (currentDestination?.route?.contains(RouteNames.Account.route) != true && currentDestination?.route?.contains(
                        RouteNames.Splash.route
                    ) != true
                ) {
                    Button(
                        onClick = { onShowDialog(true) },
                        elevation = ButtonDefaults.buttonElevation(4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddCircle,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier.padding(8.dp),
                    snackbar = { snackbarData ->
                        Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                    }
                )
            },
        ) { innerPadding ->
            //content
            Surface(
                Modifier.padding(innerPadding).fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.large,
            ) {
                Routes(
                    navHostController = navHostController,
                    taskViewModel = taskViewModel,
                    taskUiState = taskUiState,
                    onNavigationRouteNameChange = onNavigationRouteNameChange
                )
            }
        }
    }
}


@Composable
fun ExpandedScreen(
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState,
    currentDestination: NavDestination?,
    taskViewModel: TaskViewModel,
    taskUiState: TaskUiState,
    onNavigationRouteNameChange: (String) -> Unit,
    onShowDialog: (Boolean) -> Unit,
    scope: CoroutineScope
) {
    Scaffold(snackbarHost = {
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.padding(8.dp),
            snackbar = { snackbarData ->
                Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
            }
        )
    }) { innerPadding ->
        PermanentNavigationDrawer(drawerContent = {
            PermanentDrawerSheet(Modifier.width(200.dp)) {
                Column(Modifier.padding(vertical = 16.dp)) {
                    Text(
                        taskViewModel.authUser.value.email.ifEmpty { "Anonymous" },
                        Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ExtendedFloatingActionButton(
                        onClick = { onShowDialog(true) },
                        modifier = Modifier.padding(horizontal = 12.dp).height(36.dp)
                            .fillMaxWidth(),
                        elevation = FloatingActionButtonDefaults.elevation(4.dp),
                        shape = MaterialTheme.shapes.medium,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Outlined.AddCircle, null)
                    }
                    Spacer(Modifier.height(24.dp))
                    NavigationList(currentDestination?.route, onNavigationRouteNameChange)
                }
            }

        }, content = {
            VerticalDivider()
            Row(modifier = Modifier.padding(innerPadding)) {
                FirstPanel(
                    navHostController = navHostController,
                    taskViewModel = taskViewModel,
                    taskUiState = taskUiState,
                    onNavigationRouteNameChange = onNavigationRouteNameChange,
                    Modifier.width(360.dp).fillMaxHeight()
                )
                VerticalDivider()
                SecondPanel(
                    taskViewModel = taskViewModel,
                    taskUiState = taskUiState,
                    Modifier.weight(1f).fillMaxHeight()
                )
            }
        })
    }
}

@Composable
fun FirstPanel(
    navHostController: NavHostController, taskViewModel: TaskViewModel,
    taskUiState: TaskUiState, onNavigationRouteNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Routes(
            navHostController = navHostController,
            taskViewModel = taskViewModel,
            taskUiState = taskUiState,
            canNavigate = false,
            onNavigationRouteNameChange = onNavigationRouteNameChange
        )
    }
}

@Composable
fun SecondPanel(
    taskViewModel: TaskViewModel,
    taskUiState: TaskUiState, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val task = taskUiState.selectedTask
        if (task.title.isBlank()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No task selected...")
            }
        } else
            TaskDetailsScreen(
                task = task,
                onFavoriteClick = {
                    if (it.isFavorite) taskViewModel.onTaskAction(
                        TaskActions.UnmarkAsFavorite,
                        it
                    ) else taskViewModel.onTaskAction(TaskActions.MarkAsFavorite, it)
                })
    }
}