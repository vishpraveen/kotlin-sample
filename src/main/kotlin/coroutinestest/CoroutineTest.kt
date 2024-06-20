package coroutinestest

import data.Callback
import data.FruitsRepository
import data.LegacyDataSource
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CoroutineTest {

    fun exposedFunction() {
        runBlocking {
            try {
                testLaunch()
                println("Main Execution Completed for launch.")
                testAsync()
                println("Main Execution Completed for async.")
                val repository = FruitsRepository(LegacyDataSource())
                println("Main Execution started for fetchFruitsLegacyWay.")
                delay(3000)
                fetchFruitsLegacyWay(repository)
                println("Main Execution completed for fetchFruitsLegacyWay.")
                println("Main Execution started for fetchFruitsUsingCoroutines.")
                delay(3000)
                fetchFruitsUsingCoroutines(repository)
                println("Main Execution completed for fetchFruitsUsingCoroutines.")
                CoroutineScope(Dispatchers.IO).launch {
                    val fruits = fetchFruits()
                    println("Fruits: $fruits")
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private suspend fun fetchFruitsUsingCoroutines(repository: FruitsRepository) {
        suspendCoroutine { continuation ->
            repository.fetchFruits(object : Callback<List<String>> {
                override fun onSuccess(response: List<String>) {
                    continuation.resume(response)
                }

                override fun onFailure(e: Throwable) {
                    continuation.resumeWithException(e)
                }
            })
        }.let {
            println("fetchFruitsUsingCoroutines: Response: $it")
        }
    }

    private fun fetchFruitsLegacyWay(repository: FruitsRepository) {
        repository.fetchFruits(object : Callback<List<String>> {
            override fun onSuccess(response: List<String>) {
                println("fetchFruitsLegacyWay: onSuccess: $response ")
            }

            override fun onFailure(e: Throwable) {
                println("fetchFruitsLegacyWay: onFailure: ${e.message} ")
            }
        })
    }

    private suspend fun testLaunch() {
        val launchJob = CoroutineScope(Dispatchers.IO).launch {
            delay(2000L)
            println("Executing launch block")
        }

        launchJob.start() // starts the job execution
        launchJob.join() // if used makes the main tread to waits for 2000 millis to print the statement
    }

    private suspend fun testAsync() {
        val scope = CoroutineScope(context = Dispatchers.IO + Job() + CoroutineName("Some Coroutine Name"))
        scope.launch {
            //  This coroutine will run on the IO thread
        }
        val data = CoroutineScope(context = Dispatchers.IO + Job() + CoroutineName("Some Coroutine Name")).async {
            delay(2000L)
            println("Executing async block")
            "Praveen"
        }

        val result = data.await()

        println("Data is Active: ${data.isActive}")
        println("Data is Completed: ${data.isCompleted}")
        println("Result from Data: $result")
    }

    private suspend fun calledFromA(param: Int) {
        val count = param + 1
        println("calledFromA $count")
        if (count < 10) {
            delay(500L)
            calledFromB(count)
        }
    }

    private suspend fun calledFromB(param: Int) {
        val count = param + 1
        println("calledFromB $count")
        if (count >= 6) {
            throw Exception("Exception Terminated.")
        }
        if (count < 12) {
            delay(1000L)
            calledFromA(count)
        }
    }

    private suspend fun fetchFruits(): List<String> {
        delay(2000L)
        return listOf(
            "Apple",
            "Mango",
            "Banana",
            "Cherry"
        )
    }
}