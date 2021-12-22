package features
import Dataset
import kotlinx.serialization.*
import org.apache.commons.text.similarity.LevenshteinDistance
import smile.nlp.normalizer.SimpleNormalizer
import java.text.SimpleDateFormat
import java.util.*

interface Tweet{
    val text: String
    val hasImage: Boolean
}

interface Retweet : Tweet{
    val user: String
}

interface Reply : Tweet {
    val user : String
}

private open class ConcreteTweet(
    override val text: String, override val hasImage: Boolean
) : Tweet{
}

private class ConcreteReply(override val user: String, text: String, hasImage: Boolean) : ConcreteTweet(text, hasImage), Reply

private class ConcreteRetweet(
    override val user: String,
    text: String, hasImage: Boolean
) : ConcreteTweet(text, hasImage), Retweet

@Serializable
data class User(
    val ID: String,
    val profile: Profile?,
    private val tweet: List<String>? = null,
    val neighbor: Neighbor? = null,
    val domain: List<String>,
    private val label: Int? = null
){

    @Transient
    private val retweetRegex = Regex("RT @[a-zA-Z0-9]*:")
    @Transient
    private val replyRegex = Regex("@[a-zA-Z0-9]* ")

    val tweets: List<Tweet>? by lazy {
        tweet?.map {

            val hasImage = false

            val hasRetweetName = retweetRegex.find(it)
            val hasReplyName = replyRegex.find(it)

            if (hasRetweetName != null){
                val name = hasRetweetName.value.substringAfter('@').substringBefore(':')
                ConcreteRetweet(name,it.substringAfter(hasRetweetName.value),hasImage)
            } else if (hasReplyName != null) {
                val name = hasReplyName.value.substringAfter('@').substringBefore(' ')
                ConcreteReply(name, it.substringAfter(hasReplyName.value), hasImage)
            } else {
                ConcreteTweet(it, hasImage)
            }
        }
    }

    fun isBot(): Boolean? {
        return when(label){
            0 -> false
            1 -> true
            else -> null
        }
    }
    val levenshteinDistanceLessThan30: Boolean by lazy{
        if (this.tweets == null) {
            true
        } else {
            val l = LevenshteinDistance(30)
            this.tweets!!
                // cross product
                .flatMap { a ->
                    this.tweets!!.map { b-> a to b  }
                }
                .parallelStream()
                .allMatch { (a,b) ->
                    l.apply(a.text,b.text) >= 0
                }
        }
    }
}


@Serializable
data class Neighbor(
    private val following: List<String>,
    private val follower: List<String>
){
    @Transient
    lateinit var dataset : Dataset


    val followingUsers : List<User> by lazy {
        following.mapNotNull { id ->
            dataset.find {
                it.ID == id
            }
        }
    }

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
    private val favourites_count : String?,
    val default_profile : String?,
    val default_profile_image: String?,
    val profile_use_background_image: String?,
    val has_extended_profile: String?,
    val profile_location: String?,
    val statuses_count: String?
){
    @Transient
    val followers: Int = followers_count?.trim()?.toInt() ?: 0
    val following: Int = friends_count?.trim()?.toInt() ?: 0
    val likes: Int = favourites_count?.trim()?.toInt() ?: 0
    val tweets: Int = statuses_count?.trim()?.toInt() ?: 0
    val listed: Int = listed_count?.trim()?.toInt() ?: 0

    val isVerified = when (verified){
        "False " -> false
        "True " -> true
        null -> false
        else -> throw Exception("$verified ????")
    }

    val isDefaultProfile = when (default_profile){
        "False " -> false
        "True " -> true
        null -> false
        else -> throw Exception("default_profile was $default_profile for user $id")
    }

    val isDefaultProfileImage = when (default_profile_image){
        "False " -> false
        "True " -> true
        null -> false
        else -> throw Exception("default_profile_image was $default_profile_image for user $id")
    }

    val usesBackgroundImage = when (profile_use_background_image){
        "False " -> false
        "True " -> true
        null -> false
        else -> throw Exception("????")
    }

    val hasExtendedProfile = when (has_extended_profile){
        "False " -> false
        "True " -> true
        null -> false
        else -> throw Exception("????")
    }

    val hasLocation = when (location){
        " " -> false
        else -> true
    }

    val hasProfileLocation = when (profile_location){
        "None " -> false
        else -> true
    }

    val hasDescription = when (description){
        " " -> false
        else -> true
    }

    val hasScreenName = when(screen_name){
        " " -> false
        "None " -> false
        else -> true
    }

    val hasID = when(id){
        " " -> false
        "None " -> false
        else -> true
    }

    @Transient //marks field as ignore for serializer
    val creation_date : Date? = created_at?.let { date_parser.parse(it.trim()) }

    companion object{
        private val date_parser = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH)
    }
}
