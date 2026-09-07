import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.ReentrantLock

class Lotto {

    private val lottoNumbers: Set<Int> = generateLottoNumbers()
    
    val stats = IntArray(8)

    private val lock = ReentrantLock()

    private fun generateLottoNumbers(): Set<Int> {
        // Generate 7 unique numbers between 1 and 40
        val numbers = mutableSetOf<Int>()

        while (numbers.size < 7) {
            numbers.add(ThreadLocalRandom.current().nextInt(1, 41))
        }

        return numbers
    }
    // Check if the given numbers are in the lotto numbers
    fun check(numbers: List<Int>) {
        val hits = numbers.count { it in lottoNumbers }
        // Do independent work outside the lock.
        // Lock only the shared data.
        lock.lock()
        
        try {
            stats[hits]++
        } finally {
            lock.unlock()
        }
    }
}
//Generate a random guess of 7 unique numbers between 1 and 40
fun generateGuess(): List<Int> {
    val random = ThreadLocalRandom.current()
    val numbers = mutableSetOf<Int>()

    while (numbers.size < 7) {
        numbers.add(random.nextInt(1, 41))
    }

    return numbers.toList()
}

fun runGuesses(
    lotto: Lotto,
    numberOfGuesses: Int
) {// Generate and check 13.5 million guesses
    repeat(numberOfGuesses) {
        lotto.check(generateGuess())
    }
}

fun main() {
    val lotto = Lotto()
    // Generate 13,500,000 guesses
    val numberOfGuesses = 13_500_000
    val threadCount = 4
    val guessesPerThread = numberOfGuesses / threadCount

    val threads = mutableListOf<Thread>()

    val start = System.currentTimeMillis()

    repeat(threadCount) {
        val thread = Thread {
            runGuesses(lotto, guessesPerThread)
        }

        threads.add(thread)
        thread.start()
    }

    threads.forEach { it.join() }

    val end = System.currentTimeMillis()

    val processor = System.getenv("PROCESSOR_IDENTIFIER") ?: "Unknown"
    println("Processor: $processor")
    println("Running time: ${end - start} ms on $threadCount threads")

    for (i in lotto.stats.indices) {
        println("$i: ${lotto.stats[i]}")
    }
    // Check if the checksum is correct (should be 13.5 million)
    println("Checksum: ${lotto.stats.sum()}")
}