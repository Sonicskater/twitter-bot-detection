import org.apache.commons.text.similarity.LevenshteinDistance

class LevenshtienDistanceLessThan30 : BinaryFeature{
    override fun hasFeature(user: User): Boolean {

        if (user.tweet == null) {
            return true
        }

        val l = LevenshteinDistance(30)
        return user.tweet
                // cross product
            .flatMap { a ->
                user.tweet.map { b-> a to b  }
            }
            .parallelStream()
            .allMatch { (a,b) ->
                l.apply(a,b) >= 0
            }
    }
}