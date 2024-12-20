package org.example.mytask.presentation.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    splashScreenViewModel: SplashScreenViewModel = koinViewModel(),
    onNavigationRouteNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (splashScreenViewModel.showError.value) {
            Text(text = "Something wrong happened. Please try again.")
            Button(
                onClick = {
                    splashScreenViewModel.onAppStart(onNavigationRouteNameChange)
                }, shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Try again")
            }

        } else {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
                modifier = Modifier.size(64.dp),
                strokeCap = StrokeCap.Round
            )
        }
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        splashScreenViewModel.onAppStart(onNavigationRouteNameChange)
    }
}
