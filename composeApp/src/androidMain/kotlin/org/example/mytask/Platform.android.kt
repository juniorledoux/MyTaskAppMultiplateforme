package org.example.mytask

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// Android actual
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun getCalculateWindowSizeClass(configuration: Configuration): WindowWidthSize {
    val windowSizeClass = WindowSizeClass.calculateFromSize(
        DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    )
    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> WindowWidthSize.Compact
        WindowWidthSizeClass.Medium -> WindowWidthSize.Medium
        WindowWidthSizeClass.Expanded -> WindowWidthSize.Expanded
        else -> WindowWidthSize.Expanded
    }
}

/**
 * Remembers the [WindowSizeClass] calculated from the current [Activity]'s bounds.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val activity = LocalContext.current as Activity
    val configuration = LocalConfiguration.current
    return remember(activity, configuration) {
        WindowSizeClass.calculateFromSize(
            DpSize(
                configuration.screenWidthDp.dp,
                configuration.screenHeightDp.dp,
            )
        )
    }
}