package com.mny.wan.im.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.mny.wan.data.IRepository
import com.mny.wan.http.Result
import com.mny.wan.im.data.api.model.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

/**
 * Desc:
 */
abstract class BaseRepository {
    @Inject
    lateinit var mRepository: IRepository

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
//        mRepository = null
    }

    suspend fun <T : Any> apiCall(call: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return call.invoke()
    }

    suspend fun <T : Any> safeHttpRequest(call: suspend () -> Result<T>, errorMessage: String = ""): Result<T> {
        return try {
            call()
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            Result.Error(IOException(errorMessage, e))
        }
    }

    suspend fun <DATA : Any> executeRequest(dataBlock: suspend FlowCollector<Result<DATA>>.() -> Unit,
                                            successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                            errorBlock: (suspend FlowCollector<Result<DATA>>.() -> Unit)? = null,
                                            emitWhenError: Boolean = true): Result<DATA> {
        return coroutineScope {
            var result: Result<DATA> = Result.Error(Exception("初始状态"))
            flow<Result<DATA>>(dataBlock)
                    .catch { cause ->
                        if (errorBlock != null) {
                            errorBlock()
                        } else if (emitWhenError) {
                            emit(Result.Error(Exception("有错误", cause)))
                        }
                        Log.e("Mojito-Error", "executeRequest: ", cause)
                    }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        result = it
                        successBlock?.let { it() }
                    }
            result
        }
    }

    fun <Model, DomainModel : Any> resultConvert(response: BaseResponse<Model>, toDomain: (model: Model) -> DomainModel): Result<DomainModel> {
        return if (!response.isSuccess()) {
            Result.Error(IOException(response.message))
        } else {
            Result.Success(toDomain.invoke(response.result))
        }

    }
}