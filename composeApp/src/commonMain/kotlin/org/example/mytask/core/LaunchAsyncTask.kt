package org.example.mytask.core

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

suspend fun launchWithAwait(scope: CoroutineScope, block: suspend () -> Unit) {
    try {
        scope.async {
            block()
        }.await()
    } catch (e: Exception) {
        scope.async {
            SnackBarController.sendEvent(event = SnackBarEvent(message = e.message.toString()))
        }.await()
    }
}

fun launchCatching(
    snackbar: Boolean = true,
    scope: CoroutineScope,
    block: suspend () -> Unit
) {
    try {
        scope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    //show error message
                    throw (throwable)
                }
            },
        ) { block() }
    } catch (e: Exception) {
        scope.launch {
            SnackBarController.sendEvent(event = SnackBarEvent(message = e.message.toString()))
        }
    }
}
