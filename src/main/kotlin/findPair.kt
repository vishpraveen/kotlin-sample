fun findPair(arr: IntArray, target: Int) {
    val map = mutableMapOf<Int, Int>()

    arr.forEach { element ->
        val diff = target - element
        if (!map.contains(diff)) {
            map[element] = diff
        } else {
            println("Match Found: $element, $diff")
        }
    }
    println("Execution completed")
}