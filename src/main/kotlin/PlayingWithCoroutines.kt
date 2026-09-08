import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

const val N = 100
class Bank {
    class Account {
        private var amount: Double = 0.0
        private val mutex = Mutex()

        suspend fun deposit(amount: Double) {
            mutex.withLock {
                val x = this.amount
                delay(1) // simulates processing time
                this.amount = x + amount
            }
        }
        fun saldo(): Double = amount
    }
    /* Approximate measurement of the given block's execution time */
    fun withTimeMeasurement(title:String, isActive:Boolean=true, code:() -> Unit) {
        if(!isActive) return
        val time = measureTimeMillis { code() }
        println("MEASUREMENT: operation in '$title' took ${time} ms")
    }
    data class Saldos(val saldo1: Double, val saldo2: Double)
    fun bankProcess(account: Account): Saldos {
        var saldo1: Double = 0.0
        var saldo2: Double = 0.0
        /* we measure the execution time of one deposit task */
        withTimeMeasurement("Single coroutine deposit $N times") {
            runBlocking {
                launch {
                    for (i in 1..N)
                        account.deposit(1.0)
                }
            }
            saldo1 = account.saldo()
        }
        /* then we measure the execution time of two simultaneous deposit tasks using
        coroutines */
        withTimeMeasurement("Two $N times deposit coroutines together", isActive = true) {
            runBlocking {
                val job1 = launch {
                    repeat(N) { account.deposit(1.0) }
                }
                val job2 = launch {
                    repeat(N) { account.deposit(1.0) }
                }
                job1.join()
                job2.join()
                saldo2 = account.saldo()
            }
        }
        return Saldos(saldo1, saldo2)
    }
    fun tester() {
        val bank = Bank()
        val results = bank.bankProcess(Account())
        println("Saldo1: ${results.saldo1} Saldo2: ${results.saldo2}")
    }
}
fun main(args: Array<String>) {
    Bank().tester()
}