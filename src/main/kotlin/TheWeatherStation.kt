// Subject and Observer
interface Subject {
    fun registerObserver(o: Observer)
    fun removeObserver(o: Observer)
    fun notifyObservers()
}
interface Observer {
    fun update(data: WeatherData)
}
// WeatherData class contains the weather data
data class WeatherData(val temperature: Float, val humidity: Float, val pressure: Float)
// WeatherStation class is the subject in the Observer pattern
// It contains a list of observers and a current weather data object
class WeatherStation : Subject {

    private val observers: MutableList<Observer> = mutableListOf()
    override fun registerObserver(o: Observer) {
        observers.add(o)
    }
    override fun removeObserver(o: Observer) {
        observers.remove(o)
    }
    override fun notifyObservers() {
        currentData?.let { data ->
            observers.forEach { observer ->
                observer.update(data)
            }
        }
    }

    private var currentData: WeatherData? = null
    // This method is called whenever new weather data is available.
    fun measurementsChanged(newData: WeatherData) {
        this.currentData = newData
        println("WeatherStation: Got new data -> $currentData")

        notifyObservers()
    }
}
// CurrentConditionsDisplay and StatisticsDisplay classes are the observers in the Observer pattern
// They implement the Observer interface and display the current weather data
class CurrentConditionsDisplay : Observer {

    private var currentData: WeatherData? = null
    override fun update(data: WeatherData) {
        this.currentData = data
        display()
    }
    // This method displays the current weather data.
    fun display() {
        println(
            "CurrentConditionsDisplay: Current conditions: " +
                    currentData?.temperature + "C degrees and " +
                    currentData?.humidity + "% humidity " +
                    currentData?.pressure + " hPa pressure"
        )
    }
}

class StatisticsDisplay : Observer {
    private val temperatures = mutableListOf<Float>()
    var average: Float = 0.0f

    override fun update(data: WeatherData) {
        temperatures.add(data.temperature)
        display()
    }
// This method calculates and displays the average temperature.
    fun display() {
        if (temperatures.isNotEmpty()) {
            average = temperatures.sum() / temperatures.size
            println("StatisticsDisplay: Avg temperature: ${average}C")
        }
    }
}

fun main() {
    val weatherStation = WeatherStation()
    val currentConditionsDisplay = CurrentConditionsDisplay()
    val statisticsDisplay = StatisticsDisplay()

    weatherStation.registerObserver(currentConditionsDisplay)
    weatherStation.registerObserver(statisticsDisplay)

    println("--- Simulating new measurement ---")
    weatherStation.measurementsChanged(
        WeatherData(25.0f, 65f, 1012f)
    )

    println("\n--- Simulating another measurement ---")
    weatherStation.measurementsChanged(
        WeatherData(27.5f, 70f, 1011f)
    )

    println("\n--- Unregistering statistics display ---")
    weatherStation.removeObserver(statisticsDisplay)

    println("\n--- Final measurement ---")
    weatherStation.measurementsChanged(
        WeatherData(26.0f, 90f, 1013f)
    )
}

