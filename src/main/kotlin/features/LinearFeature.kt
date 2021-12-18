package features

interface LinearFeature {
    fun hasFeature(user: User) : Float
}

fun LinearFeature.cutoff(cutoff: Double = 0.5) : BinaryFeature{
    return CutoffFeature(this, cutoff = cutoff)
}
