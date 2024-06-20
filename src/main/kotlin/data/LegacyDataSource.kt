package data

class LegacyDataSource : FruitService {
    override fun fetchFruits(callback: Callback<List<String>>) {

        Thread {
            try {
                Thread.sleep(1000)
                callback.onSuccess(fruits)
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }.apply {
            start()
            join()
        }

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