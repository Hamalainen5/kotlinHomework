import kotlin.math.min
import kotlin.math.max

// Original Car class from the lecture, made open so that
// ElectricCar can inherit from it
open class Car(
    val maxSpeed: Double = 120.0,
    val gasolineCapacity: Double = 50.0
) {
    var gasolineLevel: Double = 0.0
        private set

    var speed: Double = 0.0
        protected set

    fun fillTank() {
        gasolineLevel = gasolineCapacity
    }

    open fun accelerate() {
        val newSpeed = min(speed + 1.0, maxSpeed)
        val gasolineNeeded = (newSpeed - speed) * 0.1

        if (gasolineLevel >= gasolineNeeded) {
            gasolineLevel -= gasolineNeeded
            this.speed = newSpeed
        }

    }

    open fun decelerate() {
        speed = max(speed - 1.5, 0.0)
    }
}

// ElectricCar inherits the speed and maxSpeed functionality
// from Car, but uses a battery instead of gasoline

open class ElectricCar(
    maxSpeed: Double = 120.0,
    val batteryCapacity: Double = 50.0,) : Car(maxSpeed) {

    var batteryLevel: Double = 0.0
        protected set

    // The car can only be charged when stationary
    fun charge() {
        if (speed == 0.0) {
            batteryLevel = batteryCapacity
        }
    }

    // Uses The same consumption logic as the original Car.
    override fun accelerate() {
        val newSpeed = min(speed + 1.0, maxSpeed)
        val batteryNeeded = (newSpeed - speed) * 0.1

        if (batteryLevel >= batteryNeeded) {
            batteryLevel -= batteryNeeded
            speed = newSpeed
        }
    }

    // Deceleration regenerates 20% of the energy that would
    // have been consumed when speeding up back to the old speed
    override fun decelerate() {
        val oldSpeed = speed
        val newSpeed = max(speed - 1.5, 0.0)

        // This is the amount of consumption for accelerating
        // from newSpeed to oldSpeed using the same formula as above
        val accelerationConsumption = (oldSpeed - newSpeed) * 0.1

        // 20% of that energy is recovered
        val recoveredEnergy = accelerationConsumption * 0.2

        batteryLevel = min(batteryLevel + recoveredEnergy, batteryCapacity)
        speed = newSpeed
    }
}

// ElectricBus inherits all ElectricCar functionality
// and adds a passenger functionality
class ElectricBus(
    maxSpeed: Double = 100.0,
    batteryCapacity: Double = 100.0,
): ElectricCar(maxSpeed, batteryCapacity) {
    var passengers: Int = 0
        private set

    // Negative amounts are ignored
    fun loadPassengers(amount: Int) {
        if (amount > 0) {
            passengers += amount
        }
    }

    // If more passengers are unloaded than are present,
    // the excess passengers are ignored
    fun unloadPassengers(amount: Int) {
        if (amount > 0) {
            passengers = max(passengers - amount, 0)
        }
    }

    override fun accelerate() {
        val newSpeed = min(speed + 1.0, maxSpeed)

        // Same basic consumption formula as Car/ElectricCar
        val baseConsumption = (newSpeed - speed) * 0.1

        // Each passenger increases consumption by 1%
        val passengerMultiplier: Double = 1.0 + passengers * 0.01
        val batteryNeeded = baseConsumption * passengerMultiplier

        if (batteryLevel >= batteryNeeded) {
            batteryLevel -= batteryNeeded
            speed = newSpeed
        }

    }

}


fun main() {
    println("=== ELECTRIC CAR TEST ===")
    val car = ElectricCar( maxSpeed = 120.0, batteryCapacity = 10.0 )
    println("Initial battery: ${car.batteryLevel}")
    println("Initial speed: ${car.speed}")

    // Car is stationary, so charging should work.
    car.charge()
    println("Battery after charging: ${car.batteryLevel}")

    // Accelerating should consume battery.
    car.accelerate()
    car.accelerate()
    car.accelerate()
    println("Speed after accelerating: ${car.speed}")
    println("Battery after accelerating: ${car.batteryLevel}")

    // Decelerating should regenerate some battery.
    val batteryBeforeDeceleration = car.batteryLevel
    car.decelerate()
    println("Battery after decelerating: ${car.batteryLevel}")
    println("Battery recovered: " + (car.batteryLevel - batteryBeforeDeceleration))

    // Try charging while moving
    car.charge()
    println("Battery after trying to charge while moving: ${car.batteryLevel}")

    // Stop the car and try charging again
    while (car.speed > 0.0) {
        car.decelerate()
    }
    car.charge()
    println("Speed after stopping: ${car.speed}")
    println("Battery after stopping and charging: ${car.batteryLevel}")

    println("=== ELECTRIC BUS TEST ===")
    val bus = ElectricBus( maxSpeed = 100.0, batteryCapacity = 100.0 )
    println("Initial battery: ${bus.batteryLevel}")
    println("Initial passengers: ${bus.passengers}")

    // Charge the bus
    bus.charge()
    println("Battery after charging: ${bus.batteryLevel}")

    // Accelerate with no passengers
    val batteryBeforeEmptyAcceleration = bus.batteryLevel
    bus.accelerate()
    println("Speed after accelerating: ${bus.speed}")
    println("Battery after accelerating: ${bus.batteryLevel}")
    println("Battery consumption: " + (batteryBeforeEmptyAcceleration - bus.batteryLevel))

    // Stop the bus and try charging again
    while (bus.speed > 0.0) {
        bus.decelerate()
    }
    bus.charge()
    println("Speed after stopping: ${bus.speed}")
    println("Battery after stopping and charging: ${bus.batteryLevel}")

    // Load passengers
    bus.loadPassengers(5)
    println("Passengers after loading: ${bus.passengers}")

    val batteryBeforePassengers = bus.batteryLevel

    // Accelerate with passengers
    bus.accelerate()
    println("Speed after accelerating: ${bus.speed}")
    println("Battery after accelerating: ${bus.batteryLevel}")
    println("Battery consumption: " + (batteryBeforePassengers - bus.batteryLevel))

    // Test unloading passengers
    bus.unloadPassengers(3)
    println("Passengers after unloading: ${bus.passengers}")

    // Test unloading negative passengers
    bus.unloadPassengers(-5)
    println("Passengers after unloading negative: ${bus.passengers}")

    // Test unloading more passengers than exist
    bus.unloadPassengers(100)
    println("Passengers after unloading 100: ${bus.passengers}")

}