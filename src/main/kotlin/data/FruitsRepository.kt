package data

/**
 * @fruitService is only as constructor parameter and not a class property
 * hence cannot be accessed directly into the class without storing into a local variable
 */

class FruitsRepository(
    fruitService: FruitService
) : FruitService {

    private var _service: FruitService = fruitService

    override fun fetchFruits(callback: Callback<List<String>>) {
        _service.fetchFruits(callback)
    }

    override suspend fun fetchFruits(): List<String> {
        return _service.fetchFruits()
    }
}