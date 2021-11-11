interface Distance<T> : (Array<T>, Array<T>) -> Double {
    fun distance(a : Array<T>, b : Array<T>) : Double

    override fun invoke(p1: Array<T>, p2: Array<T>): Double  = distance(p1, p2)
}