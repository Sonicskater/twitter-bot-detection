package features.smu

import features.BinaryFeature
import features.User

//Class used as the cutoff point for the distance of the total amount of differences between two strings.
//The Levenshtein distance counts the number of differences between two strings.
class LevenshteinDistanceLessThan30 : BinaryFeature {
    override fun hasFeature(user: User): Boolean {
        return user.levenshteinDistanceLessThan30
    }
}