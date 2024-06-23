package data

import kotlinx.coroutines.delay

class FruitsDataSource : FruitService {
    override fun fetchFruits(callback: Callback<List<String>>) {
        Thread {
            try {
                Thread.sleep(1000) // delay to stimulate api call
                callback.onSuccess(fruits)
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }.apply {
            start()
            join()
        }
    }

    override suspend fun fetchFruits(): List<String> {
        delay(1000) // delay to stimulate api call
        return fruits
    }

    private companion object {
        val fruits = listOf(
            "Apple",
            "Mango",
            "Cherry",
            "Banana",
            "Lemon",
            "Watermelon",
            "Sweet lime",
            "Orange",
            "Kiwi"
        )
    }
}