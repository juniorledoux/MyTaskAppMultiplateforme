package org.example.mytask

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

// Interface pour représenter la taille de la fenêtre
sealed interface WindowWidthSize {
    object Compact : WindowWidthSize
    object Medium : WindowWidthSize
    object Expanded : WindowWidthSize
}