package features.jis

import features.BinaryFeature
import features.Retweet
import features.User

//Checks if the user account retweets more than tweeting.
class RetweetsMoreThanTweets : BinaryFeature {
    override fun hasFeature(user: User): Boolean {
        val retweets = user.tweets?.filterIsInstance<Retweet>()?.count() ?: 0
        val tweets = user.tweets?.size ?: 0
        return retweets > tweets-retweets
    }
}