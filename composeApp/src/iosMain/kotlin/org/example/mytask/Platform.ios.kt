package org.example.mytask

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

// iOS/Desktop actual (Exemple pour Desktop - à adapter pour iOS)
@OptIn(ExperimentalForeignApi::class)
fun getCalculateWindowSizeClass(): WindowWidthSize {
    val screenWidth = UIScreen.mainScreen.bounds.size.toDouble()

    return when {
        screenWidth < 600 -> WindowWidthSize.Compact
        screenWidth < 960 -> WindowWidthSize.Medium
        else -> WindowWidthSize.Expanded
    }
}