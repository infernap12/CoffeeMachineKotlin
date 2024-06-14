package machine

import kotlin.math.sign

data class BeverageSystem(
    var water: Int = 400,
    var milk: Int = 540,
    var coffeeBeans: Int = 120,
    var cups: Int = 9,
    var money: Int = 550
) {
    override fun toString(): String {
        return """
        The coffee machine has:
        $water ml of water
        $milk ml of milk
        $coffeeBeans g of coffee beans
        $cups disposable cups
        $$money of money
        """.trimIndent()
    }

    fun dispense(drink: Drink) {
        (checkIngredients(drink))
        water -= drink.water
        milk -= drink.milk
        coffeeBeans -= drink.coffeeBeans
        cups--
        money += drink.cost
    }

    fun checkIngredients(drink: Drink): List<String> {
        val pairs = listOf<Pair<String, Int>>(
            Pair("water", water - drink.water),
            Pair("milk", milk - drink.milk),
            Pair("coffeeBeans", coffeeBeans - drink.coffeeBeans),
            Pair("cups", cups - 1)
        )
        return pairs.filter { it.second.sign == -1 }.map { it.first }
    }
}