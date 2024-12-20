package org.example.mytask

import androidx.compose.runtime.Composable
import org.example.mytask.di.appProvider
import org.example.mytask.presentation.BaseApp
import org.example.mytask.presentation.theme.MyTaskTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App(widthSizeClass: WindowWidthSize) {
    KoinApplication(
        application = { modules(appProvider) },
    ) {
        MyTaskTheme {
            BaseApp(widthSizeClass = widthSizeClass)
        }
    }
}
