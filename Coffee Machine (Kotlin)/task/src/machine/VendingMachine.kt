package machine

import machine.VendingMachine.IdleOptions
import kotlin.system.exitProcess

class VendingMachine {
    var state: State = State.Idle
    var tank: BeverageSystem = BeverageSystem()

    init {
        enterIdle()
    }

    // pass input to each menu method, refactor back to action //if we're in MAIN all we do is set our state to the relevant menu and wait agian i think
    fun accept(userInput: String) {
        state = when (state) {
            State.Idle -> handleIdle(userInput)
            State.Buying -> handleBuying(userInput)
            is State.Filling -> handleFilling(userInput)
        }
        if (state == State.Idle) {
            println()
            enterIdle()
        }
    }

    private fun handleFilling(userInput: String = ""): State {
        if (state is State.Idle) {
            println("Write how many ml of water you want to add:")
            return State.Filling(State.FillState.WATER)
        }
        if (userInput.toIntOrNull() == null) {
            println("Please Enter Numbers")
            return state
        }
        return when ((state as State.Filling).fillState) {
            State.FillState.WATER -> {
                tank.water += userInput.toInt()
                println("Write how many ml of milk you want to add:")
                state.next()
            }

            State.FillState.MILK -> {
                tank.milk += userInput.toInt()
                println("Write how many grams of coffee beans you want to add:")
                state.next()
            }

            State.FillState.BEANS -> {
                tank.coffeeBeans += userInput.toInt()
                println("Write how many disposable cups you want to add:")
                state.next()
            }

            State.FillState.CUPS -> {
                tank.cups += userInput.toInt()
                State.Idle
            }
        }
    }

    private fun handleBuying(userInput: String = ""): State {
        if (state is State.Idle) {
            println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
            return State.Buying
        }
        return when (userInput) {
            "back" -> {
                State.Idle
            }

            in (1..Drink.DrinkType.entries.size).map { it.toString() } -> {
                val drink = Drink.DrinkType.entries[userInput.toInt() - 1].recipe
                val missingIngredients = tank.checkIngredients(drink)
                if (missingIngredients.isEmpty()) {
                    println("I have enough resources, making you a coffee!")
                    tank.dispense(drink)
                } else {
                    println("Sorry, not enough ${missingIngredients.joinToString(", ")}!")
                }
                State.Idle
            }

            else -> {
                println("Look bud, its gotta be on the list")
                State.Buying
            }
        }
    }

    enum class IdleOptions {
        BUY,
        FILL,
        TAKE,
        REMAINING,
        EXIT;
    }

    fun handleIdle(input: String): State {
        println()
        val options = IdleOptions.entries.map { it.toString().lowercase() }
        if (options.contains(input)) {
            return when (IdleOptions.valueOf(input.uppercase())) {
                IdleOptions.BUY -> handleBuying()
                IdleOptions.FILL -> handleFilling()
                IdleOptions.TAKE -> take()
                IdleOptions.REMAINING -> {
                    println(tank.toString())
                    return State.Idle
                }

                IdleOptions.EXIT -> exitProcess(0)
            }
        } else {
            println("Nah bro, that's not in the list")
            return State.Idle
        }
    }

    fun enterIdle() {
        val options = IdleOptions.entries.map { it.toString().lowercase() }
        println("Write action (${options.joinToString(", ")}): ")

    }

    fun take(): State {
        println("I gave you $${tank.money}")
        tank.money = 0
        return State.Idle
    }
}