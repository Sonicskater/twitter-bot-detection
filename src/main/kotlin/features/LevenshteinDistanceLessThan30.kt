package features

import org.apache.commons.text.similarity.LevenshteinDistance

class LevenshteinDistanceLessThan30 : BinaryFeature{
    override fun hasFeature(user: User): Boolean {
        return user.levenshteinDistanceLessThan30
    }
}