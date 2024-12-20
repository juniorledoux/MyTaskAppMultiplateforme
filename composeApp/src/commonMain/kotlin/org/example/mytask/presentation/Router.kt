package org.example.mytask.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import org.example.mytask.presentation.components.RouteNames
import org.example.mytask.presentation.screens.account.AccountScreen
import org.example.mytask.presentation.screens.checked.CheckedScreen
import org.example.mytask.presentation.screens.favorites.FavoritesScreen
import org.example.mytask.presentation.screens.splash.SplashScreen
import org.example.mytask.presentation.screens.task_details.TaskDetailsScreen
import org.example.mytask.presentation.screens.tasks.TaskActions
import org.example.mytask.presentation.screens.tasks.TaskScreen
import org.example.mytask.presentation.screens.tasks.TaskUiState
import org.example.mytask.presentation.screens.tasks.TaskViewModel


@Composable
fun Router(navHostController: NavHostController, graphRoutes: () -> NavGraph) {
    NavHost(
        navController = navHostController,
        modifier = Modifier
            .fillMaxSize(),
        /* enterTransition = {
             slideIntoContainer(
                 AnimatedContentTransitionScope.SlideDirection.Left,
                 animationSpec = tween(500)
             )
         },
         exitTransition = {
             slideOutOfContainer(
                 AnimatedContentTransitionScope.SlideDirection.Left,
                 animationSpec = tween(500)
             )
         },
         popEnterTransition = {
             slideIntoContainer(
                 AnimatedContentTransitionScope.SlideDirection.Right,
                 animationSpec = tween(500)
             )
         },
         popExitTransition = {
             slideOutOfContainer(
                 AnimatedContentTransitionScope.SlideDirection.Right,
                 animationSpec = tween(500)
             )
         },*/
        graph = graphRoutes()
    )

}


@Composable
fun Routes(
    navHostController: NavHostController,
    taskViewModel: TaskViewModel,
    taskUiState: TaskUiState,
    canNavigate: Boolean = true,
    onNavigationRouteNameChange: (String) -> Unit,
) {
    Router(navHostController = navHostController) {
        navHostController.createGraph(startDestination = RouteNames.Splash.route) {
            composable(RouteNames.Splash.route) {
                SplashScreen(
                    onNavigationRouteNameChange = onNavigationRouteNameChange,
                    modifier = Modifier
                )
            }
            composable(RouteNames.Tasks.route) {
                TaskScreen(
                    viewModel = taskViewModel,
                    taskUiState = taskUiState,
                    onNavigationRouteNameChange = onNavigationRouteNameChange,
                    canNavigate = canNavigate,
                    modifier = Modifier
                )
            }
            if (canNavigate) {
                composable(
                    "${RouteNames.TasksDetails.route}/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getString("taskId")
                    taskViewModel.setSelectedTask(taskId.toString())
                    TaskDetailsScreen(
                        task = taskUiState.selectedTask,
                        onFavoriteClick = {
                            if (it.isFavorite) taskViewModel.onTaskAction(
                                TaskActions.UnmarkAsFavorite,
                                it
                            ) else taskViewModel.onTaskAction(TaskActions.MarkAsFavorite, it)
                        })
                }
            }

            composable(RouteNames.Checked.route) {
                CheckedScreen(
                    viewModel = taskViewModel,
                    taskUiState = taskUiState,
                    onNavigationRouteNameChange = onNavigationRouteNameChange,
                    canNavigate = canNavigate,
                    modifier = Modifier
                )
            }

            composable(RouteNames.Favorites.route) {
                FavoritesScreen(
                    viewModel = taskViewModel,
                    taskUiState = taskUiState,
                    onNavigationRouteNameChange = onNavigationRouteNameChange,
                    canNavigate = canNavigate,
                    modifier = Modifier
                )
            }

            composable(RouteNames.Account.route) {
                AccountScreen(
                    onNavigationRouteNameChange = onNavigationRouteNameChange,
                    modifier = Modifier
                )
            }

        }
    }
}