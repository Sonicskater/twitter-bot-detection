package features

interface LinearFeature {
    fun hasFeature(user: User) : Float
    fun name(): String
}
