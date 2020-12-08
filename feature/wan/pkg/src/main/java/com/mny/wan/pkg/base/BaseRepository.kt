package com.mny.wan.pkg.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.mny.wan.pkg.data.remote.model.BaseResponse
import com.mny.mojito.data.IRepository
import com.mny.mojito.http.MojitoResult
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

    suspend fun <T : Any> safeHttpRequest(call: suspend () -> MojitoResult<T>, errorMessage: String = ""): MojitoResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            MojitoResult.Error(IOException(errorMessage, e))
        }
    }

    suspend fun <DATA : Any> executeRequest(dataBlock: suspend FlowCollector<MojitoResult<DATA>>.() -> Unit,
                                            successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                            errorBlock: (suspend FlowCollector<MojitoResult<DATA>>.() -> Unit)? = null,
                                            emitWhenError: Boolean = true): MojitoResult<DATA> {
        return coroutineScope {
            var mojitoResult: MojitoResult<DATA> = MojitoResult.Error(Exception("初始状态"))
            flow<MojitoResult<DATA>>(dataBlock)
                    .catch { cause ->
                        if (errorBlock != null) {
                            errorBlock()
                        } else if (emitWhenError) {
                            emit(MojitoResult.Error(Exception("有错误", cause)))
                        }
                        Log.e("Mojito-Error", "executeRequest: ", cause)
                    }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        mojitoResult = it
                        successBlock?.let { it() }
                    }
            mojitoResult
        }
    }

    fun <Model, DomainModel : Any> resultConvert(response: BaseResponse<Model>, toDomain: (model: Model) -> DomainModel): MojitoResult<DomainModel> {
        return if (!response.isSuccess()) {
            MojitoResult.Error(IOException(response.errorMsg))
        } else {
            MojitoResult.Success(toDomain.invoke(response.data))
        }

    }
}