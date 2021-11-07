import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class User(
    val ID: String,
    val profile: Profile?,
    val tweet: List<String>? = null
)

@Serializable
data class Profile(
    val id: String?,
    val location: String?,
    val name: String?,
    val screen_name: String?,
    val description: String?,
    val followers_count : String?,
    val friends_count : String?,
    val listed_count : String?,
)