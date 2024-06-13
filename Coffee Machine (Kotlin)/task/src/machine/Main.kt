package machine

import kotlin.math.sign

val tank = Tank()

fun main() {
    while (true) {
        getAction().operation()
        println()

    }
}



data class Tank(
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

    // return a list of insufficient ingredients for a given drink,
    // an empty list would indicate that it is possible to dispense that drink
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

data class Drink(val water: Int, val milk: Int, val coffeeBeans: Int, val cost: Int)



fun getAction(): Action {
    val options = Action.entries.map { it.toString().lowercase() }
    while (true) {
        println("Write action (${options.joinToString(", ")}): ")
        val input = readln().lowercase()
        println()
        if (options.contains(input)) {
            return Action.valueOf(input.uppercase())
        } else {
            println("Nah bro, that's not in the list")
        }
    }
}

enum class Action(val operation: () -> Unit) {
    BUY({ buyMenu() }),
    FILL({ fillMenu() }),
    TAKE({ takeMenu() }),
    REMAINING({ println(tank) }),
    EXIT({ System.exit(0) });

    companion object {
        fun buyMenu() {
            var drinkIndex: Int?
            while (true) {
                println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
                var input = readln()
                if (input == "back") return
                drinkIndex = input.toIntOrNull()

                when (drinkIndex) {
                    null -> {
                        println("ayo man, pick an option")
                    }

                    !in 1..Menu.entries.size -> {
                        println("Look bud, its gotta be on the list")
                    }

                    else -> {
                        drinkIndex-- // OK maybe TJ is right
                        break
                    }
                }
            }

            val drink = Menu.entries[drinkIndex].recipe
            val missingIngredients = tank.checkIngredients(drink)
            if (missingIngredients.isEmpty()) {
                println("I have enough resources, making you a coffee!")
                tank.dispense(drink)
            } else {
                println("Sorry, not enough ${missingIngredients.joinToString(", ")}!")
            }
        }

        fun fillMenu() {
            println("Write how many ml of water you want to add:")
            tank.water += readln().toInt()
            println("Write how many ml of milk you want to add:")
            tank.milk += readln().toInt()
            println("Write how many grams of coffee beans you want to add:")
            tank.coffeeBeans += readln().toInt()
            println("Write how many disposable cups you want to add:")
            tank.cups += readln().toInt()
        }

        fun takeMenu() {
            println("I gave you $${tank.money}")
            tank.money = 0
        }
    }
}

enum class Menu(val recipe: Drink) {
    ESPRESSO(Drink(250, 0, 16, 4)),
    LATTE(Drink(350, 75, 20, 7)),
    CAPPUCCINO(Drink(200, 100, 12, 6));
}