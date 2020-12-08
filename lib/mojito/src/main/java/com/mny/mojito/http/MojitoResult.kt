package com.mny.mojito.http

/**
 * 密封类：子类可以是任意的类: 数据类、Kotlin 对象、普通的类，甚至也可以是另一个密封类。
 * 但不同于抽象类的是，您必须把层级声明在同一文件中，或者嵌套在类的内部。
 */
sealed class MojitoResult<out T : Any> {
    object Loading : MojitoResult<Nothing>()
    data class Success<out T : Any>(val data: T?) : MojitoResult<T>()
    data class Error(val exception: Exception) : MojitoResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [MojitoResult] is of type [Success] & holds non-null [Success.data].
 */
val MojitoResult<*>.succeeded
    get() = this is MojitoResult.Success && data != null

inline fun <reified T : Any> MojitoResult<T>.doSuccess(success: (data: T?) -> Unit) {
    if (this is MojitoResult.Success) {
        success.invoke(data)
    }
}

inline fun <reified T : Any> MojitoResult<T>.doFailure(failure: (Throwable?) -> Unit) {
    if (this is MojitoResult.Error) {
        failure.invoke(exception)
    }
}

inline fun <reified T : Any> MojitoResult<T>.doLoading(loading: () -> Unit) {
    if (this is MojitoResult.Loading) {
        loading.invoke()
    }
}

// TODO
inline fun <reified T : Any> MojitoResult<T>.doComplete(completer: () -> Unit) {
    if (this is MojitoResult.Loading) {
        completer.invoke()
    }
}
