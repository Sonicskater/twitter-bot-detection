import features.LinearFeature

class CutoffFeature(private val linearFeature: LinearFeature, private val cutoff: Double = 0.5) : BinaryFeature {
    override fun hasFeature(user: User): Boolean {
        return linearFeature.hasFeature(user) >= cutoff
    }
}