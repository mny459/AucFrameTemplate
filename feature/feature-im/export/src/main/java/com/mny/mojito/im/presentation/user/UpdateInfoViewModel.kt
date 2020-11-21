package com.mny.mojito.im.presentation.user

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.mny.mojito.im.R
import com.mny.mojito.im.data.api.model.user.UserUpdateModel
import com.mny.mojito.im.domain.usecase.UserUseCase
import com.mny.mojito.http.Result
import com.mny.mojito.im.data.db.entity.UserEntity
import com.mny.mojito.mvvm.BaseViewAction
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.mojito.mvvm.BaseViewState
import kotlinx.coroutines.launch

/**
 * Desc:
 */
internal class UpdateInfoViewModel @ViewModelInject constructor(private val mUserUseCase: UserUseCase) : BaseViewModel<BaseViewState, BaseViewAction>(BaseViewState()) {
    override fun onReduceState(viewAction: BaseViewAction): BaseViewState = onReduceViewState(viewAction)
    fun updateUserInfo(portraitPath: String?, man: Boolean, desc: String) {
        if (TextUtils.isEmpty(portraitPath) || TextUtils.isEmpty(desc)) {
            sendCompleteAction(StringUtils.getString(R.string.data_account_update_invalid_parameter))
            return
        }
        sendLoadingAction()
        viewModelScope.launch {
            // TODO 上传图片到OSS
            val uploadImage = "https://hbimg.huabanimg.com/792b67371615d77297f7b2573134cc40ce08c7583d711-H5T1gu_fw658/format/webp"
            //UploadHelper.uploadPortrait(portraitPath!!)
            LogUtils.d("updateUserInfo $portraitPath")
            LogUtils.d("updateUserInfo $uploadImage")
            when (val result = mUserUseCase.updateUserInfo(UserUpdateModel(name = "",
                    portrait = portraitPath!!, desc = desc,
                    sex = if (man) UserEntity.SEX_MAN else UserEntity.SEX_WOMAN))) {
                is Result.Success -> {
                    result.data?.apply {

                    }
                    sendCompleteAction()
                }
                is Result.Error -> {
                    sendCompleteAction()
                }
            }
        }

    }
}