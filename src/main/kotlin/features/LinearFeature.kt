package features

import User

interface LinearFeature {
    fun hasFeature(user: User) : Float
}
