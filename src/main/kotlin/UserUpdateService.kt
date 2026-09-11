import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
data class User(val id: Int, val name: String, val status: String)
// Mock data source - DO NOT MODIFY
val allUsers = listOf(
    User(1, "Alice", "inactive"),
    User(2, "Bob", "active"),
    User(3, "Charlie", "inactive"),
    User(4, "Diana", "inactive")
)
// A mock function that simulates fetching user data from a network.
// It randomly changes a user's status to "active".
// It will throw an error after a few successful calls.
private var fetchCount = 0
fun fetchUsers(): List<User> {
    println("Fetching user data...")
    fetchCount++
    if (fetchCount > 4) {
        throw IOException("Simulated network error: Server is down.")
    }
// Create a mutable copy to modify
    val updatedUsers = allUsers.map { it.copy() }.toMutableList()
// Randomly set one inactive user to active
    val userToActivate = updatedUsers.filter { it.status == "inactive" }.randomOrNull()
    userToActivate?.let {
        val index = updatedUsers.indexOf(it)
        updatedUsers[index] = it.copy(status = "active")
    }
    return updatedUsers
}
// Main function where you will write your solution
fun main() = runBlocking {
    val userFlow = flow {
        while (true) {
            emit(fetchUsers())
            delay(2000) // Wait for 2 seconds before fetching again
        }
    }
    userFlow.transform { users ->
        users.forEach { user ->
            emit(user)
        }
    }
        .filter { it.status == "active" }
        .distinctUntilChanged()
        .catch { e ->
            println("Error fetching updates: ${e.message}")
        }
        .collect { user ->
            println("Update: ${user.name} is now active")
        }
}