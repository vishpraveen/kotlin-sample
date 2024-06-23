package coroutinestest

import data.Callback
import data.FruitsRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun suspendCoroutineTest(repository: FruitsRepository): List<String> = suspendCoroutine { continuation ->
    repository.fetchFruits(object : Callback<List<String>> {
        override fun onSuccess(response: List<String>) {
            continuation.resume(response) // Resume coroutine with the result
        }

        override fun onFailure(e: Throwable) {
            continuation.resumeWithException(e) // Resume coroutine with an exception
        }
    })
}

suspend fun suspendCancellableCoroutineTest(repository: FruitsRepository): List<String> =
    suspendCancellableCoroutine { continuation ->
        repository.fetchFruits(object : Callback<List<String>> {
            override fun onSuccess(response: List<String>) {
                continuation.resume(response) // Resume with result
            }

            override fun onFailure(e: Throwable) {
                continuation.resumeWithException(e) // Resume with an exception
            }
        })

        // Handle cancellation
        continuation.invokeOnCancellation {
            // Clean up resources or cancel async operation
        }
    }
