package coroutinestest

import data.Callback
import data.FruitsDataSource
import data.FruitsRepository
import kotlinx.coroutines.*

class CoroutineTest {

    fun exposedFunction() {
        runBlocking {
            try {
                executor(::testLaunch.name, ::testLaunch)

                val repository = FruitsRepository(FruitsDataSource())

                executor(blockName = ::fetchFruitsLegacyWay.name, block = { (::fetchFruitsLegacyWay)(repository) })

                executor(
                    blockName = ::fetchFruitsUsingCoroutine.name,
                    block = { fetchFruitsUsingCoroutine(repository) }
                )

                executor(
                    blockName = ::fetchFruitsUsingCoroutineAsyncAwait.name,
                    block = { fetchFruitsUsingCoroutineAsyncAwait(repository) }
                )

                suspendCoroutineTest(repository).let {
                    println("suspendCoroutineTest: Response: $it")
                }
                suspendCancellableCoroutineTest(repository).let {
                    println("suspendCancellableCoroutineTest: Response: $it")
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val fruits = fetchFruits()
                    println("Fruits: $fruits")
                }
            } catch (e: Exception) {
                println(e.message)
            }
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

    private suspend fun fetchFruitsUsingCoroutine(repository: FruitsRepository) {
        try {
            println("fetchFruitsUsingCoroutine success:${repository.fetchFruits()}")
        } catch (ex: Exception) {
            println("fetchFruitsUsingCoroutine failure:${ex.message}")
        }
    }

    private suspend fun fetchFruitsUsingCoroutineAsyncAwait(repository: FruitsRepository) {
        runBlocking {
            val fruits1 = async { repository.fetchFruits() }
            val data1 = fruits1.await()
            println("Data1: $data1")

            val fruits2 = async { repository.fetchFruits() }
            val data2 = fruits2.await()
            println("Data2: $data2")
        }
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
            "Apple", "Mango", "Banana", "Cherry"
        )
    }

    private suspend fun executor(blockName: String, block: suspend () -> Unit) {
        println("Main Execution Started for $blockName.")
        block.invoke()
        println("Main Execution Completed for $blockName.")
    }
}