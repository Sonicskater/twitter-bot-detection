package features.smu

import features.BinaryFeature
import features.User

class LevenshteinDistanceLessThan30 : BinaryFeature {
    override fun hasFeature(user: User): Boolean {
        return user.levenshteinDistanceLessThan30
    }
}