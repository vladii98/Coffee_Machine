package machine
import java.util.Scanner

enum class Coffee(val description: String, val water: Int, val milk: Int, val coffeeBeans: Int, val price: Int){
    ESPRESSO("Espresso", 250, 0, 16, 4),
    LATTE("Latte", 350, 75, 20, 7),
    CAPPUCINO("Cappucino", 200, 100, 12, 6)
}

enum class State() {
    CHOOSE_ACTION, BUY, FILL, TAKE, SHOW_STATE, EXIT,
}

class CoffeeMachine (){
    var income: Int = 550
    var waterSupply: Int = 400
    var milkSupply: Int = 540
    var beansSupply = 120
    var cupSupply: Int = 9
    var machineState: State = State.CHOOSE_ACTION

    fun work() {
        this.machineState = when (this.machineState) {
            State.CHOOSE_ACTION -> this.askForAction()
            State.BUY -> this.buy()
            State.FILL -> this.fill()
            State.TAKE -> this.take()
            State.SHOW_STATE -> this.outputStatus()
            else -> throw RuntimeException("Unknown state")
        }
    }

    fun askForAction(): State {
        println("Write action (buy, fill, take, remaining, exit): ")
        var action: String = readln()
        return when(action) {
            "buy" -> State.BUY
            "fill" -> State.FILL
            "take" -> State.TAKE
            "remaining" -> State.SHOW_STATE
            "exit" -> State.EXIT
            else -> {
                println("Incorrect Action")
                State.CHOOSE_ACTION
            }
        }
    }

    fun buy(): State {
        println("What do you want to buy?" +
                "1 - espresso, 2 - latte, 3 - cappuccino, " +
                "back - to main menu: ")
        val choiceOfCoffee: String = readln()
        when(choiceOfCoffee) {
            1.toString() -> this.make(Coffee.ESPRESSO)
            2.toString() -> this.make(Coffee.LATTE)
            3.toString() -> this.make(Coffee.CAPPUCINO)
        }
        return State.CHOOSE_ACTION
    }

    fun make(coffee: Coffee): State {
        if (this.enoughSupplies(coffee)) {
            --this.cupSupply
            this.waterSupply -= coffee.water
            this.milkSupply -= coffee.milk
            this.beansSupply -= coffee.coffeeBeans
            this.income += coffee.price
        }
        return State.CHOOSE_ACTION
    }

    fun enoughSupplies(coffee: Coffee): Boolean {
        if (this.waterSupply < coffee.water || this.milkSupply < coffee.milk ||
            this.beansSupply < coffee.coffeeBeans || this.cupSupply < 1) {
            if (this.waterSupply < coffee.water) println("Sorry, not enough water!")
            if (this.milkSupply < coffee.milk) println("Sorry, not enough milk!")
            if (this.beansSupply < coffee.coffeeBeans) println("Sorry, not enough beans!")
            if (this.cupSupply < 1) println("Sorry, not enough disposable cups!")
            return false
        } else {
            println("I have enough resources, making you a coffee!")
            return true
        }
    }

    fun fill(): State {
        println("Write how many ml of water you want to add: ")
        val addedWater: Int = readln().toInt()
        this.waterSupply += addedWater
        println("Write how many ml of milk you want to add: ")
        val addedMilk: Int = readln().toInt()
        this.milkSupply += addedMilk
        println("Write how many grams of coffee beans you want to add: ")
        val addedBeans: Int = readln().toInt()
        this.beansSupply += addedBeans
        println("Write how many disposable cups you want to add: ")
        val addedCups: Int = readln().toInt()
        this.cupSupply += addedCups

        return State.CHOOSE_ACTION
    }

    fun take(): State {
        println("I gave you \$${this.income}")
        this.income = 0
        return State.CHOOSE_ACTION
    }

    fun outputStatus(): State {
        val status: String = """
        The coffee machine has:
        ${this.waterSupply} ml of water
        ${this.milkSupply} ml of milk
        ${this.beansSupply} g of coffee beans
        ${this.cupSupply} disposable cups
        $${this.income} of money 
    """.trimIndent()
        println(status)
        return State.CHOOSE_ACTION
    }
}

fun main() {
    val machine = CoffeeMachine()
    do {
        machine.work()
    } while (machine.machineState != State.EXIT)
}
