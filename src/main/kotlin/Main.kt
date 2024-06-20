import apitest.HttpApiTest
import coroutinestest.CoroutineTest
import kotlinx.coroutines.runBlocking


fun main() {
    println("Hello World!")
    CoroutineTest().run { exposedFunction() }
    HttpApiTest().run {
        runBlocking {
            makeGetApiCall()
        }
    }
}


