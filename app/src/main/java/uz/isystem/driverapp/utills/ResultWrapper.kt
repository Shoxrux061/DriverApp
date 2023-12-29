package uz.isystem.driverapp.utills


sealed class ResultWrapper<out S, out E> {
    data class Success<out S>(val code: Int? = null, val data: S? = null) :
        ResultWrapper<S, Nothing>()

    data class Error<out E>(val code: Int? = null, val message: E? = null) :
        ResultWrapper<Nothing, E>()

    data class NetworkError(val code: Int? = null, val message: String? = null) :
        ResultWrapper<Nothing, Nothing>()
}