package id.depok.posyandu.ui.auth

import android.app.Application
import androidx.lifecycle.MutableLiveData
import id.depok.posyandu.base.BaseState
import id.depok.posyandu.base.BaseViewModel
import id.depok.posyandu.data.models.response.KaderDetailResponse
import id.depok.posyandu.data.models.response.LoginResponse
import id.depok.posyandu.data.repositories.AuthRepository
import id.depok.posyandu.data.repositories.KaderRepository
import id.depok.posyandu.utils.extension.defaultValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class AuthViewModel(application: Application) : BaseViewModel(application) {

    val loginState = MutableLiveData<BaseState<LoginResponse>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val kaderState = MutableLiveData<BaseState<KaderDetailResponse>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )


    fun login(email: String, password: String) = scope.launch {
        try {
            loginState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                AuthRepository(getApplication()).login(email, password)
            }
            loginState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            loginState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getKader(userId: String) = scope.launch {
        try {
            kaderState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
               KaderRepository(getApplication()).getUserKader(userId)
            }
            kaderState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kaderState.value = BaseState(loading = false, error = e, data = null)
        }
    }


}