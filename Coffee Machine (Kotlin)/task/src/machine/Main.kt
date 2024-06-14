package machine

fun main() {
    var vendingMachine = VendingMachine()
    while (true) {
        vendingMachine.accept(readln())
    }
}


