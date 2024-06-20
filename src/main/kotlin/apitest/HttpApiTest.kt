package apitest

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class HttpApiTest {

    suspend fun makeGetApiCall() {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get("https://ktor.io/")
//        val response: HttpResponse = client.get("https://dummyjson.com/products")
        println("Response: $response")
//        println("Response: ${response.call.body()}")
        client.close()
    }
}