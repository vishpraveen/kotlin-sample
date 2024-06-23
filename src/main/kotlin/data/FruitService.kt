package data

interface FruitService {
    fun fetchFruits(callback: Callback<List<String>>)
    suspend fun fetchFruits(): List<String>
}