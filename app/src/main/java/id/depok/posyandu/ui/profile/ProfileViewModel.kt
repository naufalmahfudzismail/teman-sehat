package id.depok.posyandu.ui.profile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import id.depok.posyandu.base.BaseState
import id.depok.posyandu.base.BaseViewModel
import id.depok.posyandu.data.models.Kader
import id.depok.posyandu.data.models.Skill
import id.depok.posyandu.data.models.request.KaderRequest
import id.depok.posyandu.data.models.request.SkillRequest
import id.depok.posyandu.data.models.response.KaderDetailResponse
import id.depok.posyandu.data.repositories.AuthRepository
import id.depok.posyandu.data.repositories.KaderRepository
import id.depok.posyandu.utils.extension.defaultValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(application: Application) : BaseViewModel(application) {

    val kaderState = MutableLiveData<BaseState<KaderDetailResponse>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val kaderUpdateState = MutableLiveData<BaseState<Kader>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val skillState = MutableLiveData<BaseState<Skill>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    fun getKader(kaderId: String) = scope.launch {
        try {
            kaderState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                KaderRepository(getApplication()).getKader(kaderId)
            }
            kaderState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kaderState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun updateKader(kaderId: String, request: KaderRequest) = scope.launch {
        try {
            kaderUpdateState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                KaderRepository(getApplication()).updateKader(kaderId, request)
            }
            kaderUpdateState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kaderUpdateState.value = BaseState(loading = false, error = e, data = null)
        }
    }


    fun createSkill(req: SkillRequest) = scope.launch {
        try {
            skillState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                KaderRepository(getApplication()).createSkillKader(req)
            }
            skillState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            skillState.value = BaseState(loading = false, error = e, data = null)
        }
    }
}