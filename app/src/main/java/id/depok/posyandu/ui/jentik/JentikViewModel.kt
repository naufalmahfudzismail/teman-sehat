package id.depok.posyandu.ui.jentik

import android.app.Application
import androidx.lifecycle.MutableLiveData
import id.depok.posyandu.base.BaseState
import id.depok.posyandu.base.BaseViewModel
import id.depok.posyandu.data.models.Keluarga
import id.depok.posyandu.data.models.request.PjbRequest
import id.depok.posyandu.data.repositories.PjbRepository
import id.depok.posyandu.utils.extension.defaultValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JentikViewModel  (application: Application): BaseViewModel(application) {

    val keluargaState = MutableLiveData<BaseState<Keluarga>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val pjbState = MutableLiveData<BaseState<PjbRequest>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )


    fun getKeluarga(kk : String) = scope.launch {
        try {
            keluargaState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PjbRepository(getApplication()).getKeluarga(kk)
            }
            keluargaState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            keluargaState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun createPJb(req : PjbRequest) = scope.launch {
        try {
            pjbState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PjbRepository(getApplication()).createPJB(req)
            }
            pjbState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            pjbState.value = BaseState(loading = false, error = e, data = null)
        }
    }
}