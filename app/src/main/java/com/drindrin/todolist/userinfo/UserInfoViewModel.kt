package com.drindrin.todolist.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drindrin.todolist.models.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel: ViewModel() {
    private val repository = UserInfoRepository()
    private var _userInfo = MutableStateFlow(UserInfo("","","",""))
    val userInfo: StateFlow<UserInfo> = _userInfo

    init{
        viewModelScope.launch {
            val info = repository.getInfo()
            if(info != null){
                _userInfo.value = info
            }
        }

    }
    fun updateAvatar(part: MultipartBody.Part) {
        viewModelScope.launch {
            val info = repository.updateAvatar(part)
            if(info != null){
                _userInfo.value = info
            }
        }
    }

     fun updateUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            val info = repository.updateUserInfo(userInfo)
            if(info != null){
                _userInfo.value = info
            }
        }
    }

}