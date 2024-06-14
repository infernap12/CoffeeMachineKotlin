package machine

fun main() {
    var vendingMachine = VendingMachine()
    while (true) {
        val userInput = readln()
        vendingMachine.accept(userInput)
    }
}


