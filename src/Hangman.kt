data class Parameters(var player: String, var word: CharArray, var lives: Int) {
    var mask = BooleanArray(word.size)
}

class Hangman {

    private lateinit var parameters: Parameters
    private var usedChard: MutableList<Char> = ArrayList()
    private var results: MutableMap<String, Int> = LinkedHashMap()

    /**
     * Start game lifecycle.
     */
    fun start() {
        usedChard = ArrayList()
        parameters = askParameters()
        println("\nGame is starting...")
        process()
    }

    /**
     * Process game events.
     */
    private fun process() {
        loop@ while (true) {
            while (parameters.lives > 0) {
                printCurrentStatus()
                checkInput()
                println()
            }

            parameters.lives = 0
            print("Want to play again? (Y/N/H): ")
            when (readLine().toString()) {
                "y", "Y" -> start()
                "h", "H" -> printResults()
                else -> {
                    println("Thanks for playing ${parameters.player}!")
                    break@loop
                }
            }
        }
    }

    /**
     * Ask user to input character and decide if it is correct.
     */
    private fun checkInput() {
        var char: String
        while (true) {
            print("Enter character: ")
            char = readLine().toString().toLowerCase()

            if (char.isEmpty()) {
                print("Character can't be empty! ")
                continue
            }

            if (1 != char.length) {
                print("Invalid character! ")
                continue
            }

            if (usedChard.contains(char[0])) {
                println("You already tried this character! ")
                continue
            }

            break
        }
        usedChard.add(char[0])

        if (parameters.word.contains(char[0])) {
            println("Yes, it is there!!!")
            for (i in parameters.word.indices) {
                if (char[0] == parameters.word[i]) {
                    parameters.mask[i] = true
                }
            }

            if (!parameters.mask.contains(false)) {
                results[parameters.player] = parameters.lives

                println("\nCongratulations ${parameters.player}!")
                printCurrentStatus(false)

                parameters.lives = 0
            }
        } else {
            println("There is no such character")
            parameters.lives--

            if (0 == parameters.lives) {
                println("\nSorry, ${parameters.player}, you los... The word was: " + parameters.word.joinToString(""))
            }
        }
    }

    /**
     * Ask game parameters suc as player name, secret word and remained lives.
     *
     * @return New object of Parameters class.
     */
    private fun askParameters(): Parameters {
        var player = ""
        var word = ""
        var lives: Int

        print("Enter Name: ")
        while ({ player = readLine().toString(); player }().isEmpty()) {
            print("Name can't be empty! Enter Name: ")
        }

        println("Hello ${player}. Let’s play Hangman!")


        print("Enter Word: ")
        while ({ word = readLine().toString().toLowerCase(); word }().isEmpty()) {
            print("Word can't be empty! Enter Word: ")
        }

        while (true) {
            print("Lives remaining: ")
            val livesStr = readLine().toString();

            if (livesStr.isEmpty()) {
                continue
            }

            try {
                lives = livesStr.toInt()
            } catch (e: Exception) {
                print("Lives must be a valid number! ")
                continue
            }

            if (0 >= lives) {
                print("Lives must be greater than 0! ")
                continue
            }

            break
        }

        return Parameters(player, word.toCharArray(), lives)
    }

    /**
     * Print Lives remaining and Current Word.
     *
     * @param printLives If true print Lives remaining too, else only Current Word.
     */
    private fun printCurrentStatus(printLives: Boolean = true) {
        if (printLives) {
            println("Lives remaining: ${parameters.lives}")
        }

        print("Current Word is: ")
        for (i in parameters.word.indices) {
            if (parameters.mask[i]) {
                print(parameters.word[i])
            } else {
                print("_")
            }
        }

        println()
    }

    /**
     * Print top five players result.
     */
    private fun printResults() {
        println("Results")
        results.toList()
                .sortedBy { (_, value) -> value }
                .reversed()
                .take(5)
                .map { println("${it.first} ${it.second}") }
    }

}
