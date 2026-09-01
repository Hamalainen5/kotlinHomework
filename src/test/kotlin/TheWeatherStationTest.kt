import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TestObserver : Observer {
    var receivedData: WeatherData? = null

    override fun update(data: WeatherData) {
        receivedData = data
    }
}

internal class TheWeatherStationTest {
    @Test
    fun observerReceivesWeatherData() {
        val weatherStation = WeatherStation()
        val observer = TestObserver()

        weatherStation.registerObserver(observer)

        val weatherData = WeatherData(10f, 20f, 1000f)
        weatherStation.measurementsChanged(weatherData)

        assertEquals(weatherData, observer.receivedData)
    }

    @Test
    fun multipleObserversReceivesWeatherData() {
        val weatherStation = WeatherStation()
        val observer1 = TestObserver()
        val observer2 = TestObserver()

        weatherStation.registerObserver(observer1)
        weatherStation.registerObserver(observer2)

        val weatherData = WeatherData(10f, 20f, 1000f)

        weatherStation.measurementsChanged(weatherData)

        assertEquals(weatherData, observer1.receivedData)
        assertEquals(weatherData, observer2.receivedData)
    }

    @Test
    fun removedObserverDoesNotReceiveWeatherData() {
        val weatherStation = WeatherStation()
        val observer = TestObserver()

        weatherStation.registerObserver(observer)
        weatherStation.removeObserver(observer)

        val weatherData = WeatherData(10f, 20f, 1000f)
        weatherStation.measurementsChanged(weatherData)

        assertEquals(null, observer.receivedData)
    }

    @Test
    fun statisticsDisplayCalculatesAverage() {
        val weatherStation = WeatherStation()
        val statisticsDisplay = StatisticsDisplay()

        weatherStation.registerObserver(statisticsDisplay)

        val weatherData1 = WeatherData(10f, 20f, 1000f)
        val weatherData2 = WeatherData(20f, 30f, 1001f)
        val weatherData3 = WeatherData(30f, 40f, 1002f)

        weatherStation.measurementsChanged(weatherData1)
        weatherStation.measurementsChanged(weatherData2)
        weatherStation.measurementsChanged(weatherData3)

        assertEquals(20.0f, statisticsDisplay.average, 0.1f)
    }

    @Test
    fun statisticsDisplayAverageWithOneMeasurement() {
        val weatherStation = WeatherStation()
        val statisticsDisplay = StatisticsDisplay()

        weatherStation.registerObserver(statisticsDisplay)

        val weatherData = WeatherData(10f, 20f, 1000f)
        weatherStation.measurementsChanged(weatherData)

        assertEquals(10.0f, statisticsDisplay.average, 0.1f)
    }
}
