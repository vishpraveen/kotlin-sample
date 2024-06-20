package data

/**
 * @fruitService is only as constructor parameter and not a class property
 * hence cannot be accessed directly into the class without storing into a local variable
 */

class FruitsRepository(
    fruitService: FruitService
) {

    private var _service: FruitService = fruitService

    fun fetchFruits(callback: Callback<List<String>>) {
        _service.fetchFruits(callback)
    }
}