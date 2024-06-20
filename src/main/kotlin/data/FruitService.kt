package data

interface FruitService {
    fun fetchFruits(callback: Callback<List<String>>)
}