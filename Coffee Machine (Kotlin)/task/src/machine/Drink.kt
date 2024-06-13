package machine

data class Drink(val water: Int, val milk: Int, val coffeeBeans: Int, val cost: Int) {
    enum class DrinkType(val recipe: Drink) {
        ESPRESSO(Drink(250, 0, 16, 4)),
        LATTE(Drink(350, 75, 20, 7)),
        CAPPUCCINO(Drink(200, 100, 12, 6));
    }
}