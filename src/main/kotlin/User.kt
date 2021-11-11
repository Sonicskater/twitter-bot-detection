import kotlinx.serialization.*

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
){
    val followers: Int = followers_count?.trim()?.toInt() ?: 0
    val following: Int = friends_count?.trim()?.toInt() ?: 0
}