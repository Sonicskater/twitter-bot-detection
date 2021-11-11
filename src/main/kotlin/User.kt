import kotlinx.serialization.*
import java.text.SimpleDateFormat
import java.util.*


@Serializable
data class User(
    val ID: String,
    val profile: Profile?,
    val tweet: List<String>? = null,
    val neighbor: Neighbor? = null,
    private val label: Int? = null
){
    fun isBot(): Boolean {
        return when(label){
            0 -> false
            1 -> true
            else -> throw Exception("Invalid label variable")
        }
    }
}

@Serializable
data class Neighbor(
    val following: List<String>,
    val follower: List<String>
) {

}

@Serializable
data class Profile(
    val id: String?,
    val location: String?,
    val name: String?,
    val screen_name: String?,
    val description: String?,
    private val followers_count : String?,
    val friends_count : String?,
    val listed_count : String?,
    private val created_at: String?,
    private val verified : String?,
    private val favourites_count : String?
){
    @Transient
    val followers: Int = followers_count?.trim()?.toInt() ?: 0
    val following: Int = friends_count?.trim()?.toInt() ?: 0
    val likes: Int = favourites_count?.trim()?.toInt() ?: 0

    val isVerified = when (verified){
        "False " -> false
        "True " -> true
        null -> false
        else -> throw Exception("$verified ????")
    }

    @Transient //marks field as ignore for serializer
    val creation_date : Date? = created_at?.let { date_parser.parse(it.trim()) }

    companion object{
        private val date_parser = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
    }
}
