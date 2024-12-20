package org.example.mytask


class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

fun getCalculateWindowSizeClass(): WindowWidthSize {
    // Exemple pour Desktop utilisant java.awt.Toolkit
    val screenSize = java.awt.Toolkit.getDefaultToolkit().screenSize
    val width = screenSize.width
    return when {
        width < 600 -> WindowWidthSize.Compact
        width < 960 -> WindowWidthSize.Medium
        else -> WindowWidthSize.Expanded
    }
}