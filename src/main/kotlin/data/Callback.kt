package data

interface Callback<T> {
    fun onSuccess(response: T)
    fun onFailure(e: Throwable)
}