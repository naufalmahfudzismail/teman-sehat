package id.depok.posyandu.ui.posyandu

import android.app.Application
import androidx.lifecycle.MutableLiveData
import id.depok.posyandu.base.BaseState
import id.depok.posyandu.base.BaseViewModel
import id.depok.posyandu.data.models.*
import id.depok.posyandu.data.models.request.*
import id.depok.posyandu.data.models.response.PosyanduDetailResponse
import id.depok.posyandu.data.repositories.PosyanduRepository
import id.depok.posyandu.utils.extension.defaultValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PosyanduViewModel(application: Application) : BaseViewModel(application) {

    val posyanduState = MutableLiveData<BaseState<PosyanduDetailResponse>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val posyanduUpdateState = MutableLiveData<BaseState<Posyandu>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val posyanduListState = MutableLiveData<BaseState<List<Posyandu>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val kecamatanListState = MutableLiveData<BaseState<List<Kecamatan>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val kelurahanListState = MutableLiveData<BaseState<List<Kelurahan>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )


    val kitListState = MutableLiveData<BaseState<List<Kit>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val kitRefListState = MutableLiveData<BaseState<List<Kit>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val kitState = MutableLiveData<BaseState<Kit>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val updateKitState = MutableLiveData<BaseState<Kit>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )


    val scheduleListState = MutableLiveData<BaseState<List<Jadwal>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )


    val scheduleState = MutableLiveData<BaseState<Jadwal>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val updateScheduleState = MutableLiveData<BaseState<Jadwal>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val serviceState = MutableLiveData<BaseState<Pelayanan>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val updateServiceState = MutableLiveData<BaseState<Pelayanan>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )

    val serviceListState = MutableLiveData<BaseState<List<Pelayanan>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )


    val kaderListState = MutableLiveData<BaseState<List<Kader>>>().defaultValue(
        BaseState(loading = false, error = null, data = null)
    )


    fun getPosyandu(modulId: String) = scope.launch {
        try {
            posyanduState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getPosyandu(modulId)
            }
            posyanduState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            posyanduState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getListPosyandu(kecamatan: String, kelurahan: String) = scope.launch {
        try {
            posyanduListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getPosyandu(kecamatan, kelurahan)
            }
            posyanduListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            posyanduListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun updatePosyandu(modulId: String, request: PosyanduRequest, file : File?) = scope.launch {
        try {
            posyanduUpdateState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                if(file == null) {
                    PosyanduRepository(getApplication()).updatePosyandu(modulId, request)
                } else {
                    PosyanduRepository(getApplication()).updatePosyandu(modulId, request, file)
                }
            }
            posyanduUpdateState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            posyanduUpdateState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getKecamatan() = scope.launch {
        try {
            kecamatanListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getKecamatan()
            }
            kecamatanListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kecamatanListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getKelurahan(kecamatan: String) = scope.launch {
        try {
            kelurahanListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getKelurahan(kecamatan)
            }
            kelurahanListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kelurahanListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getKelurahan() = scope.launch {
        try {
            kelurahanListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getKelurahan()
            }
            kelurahanListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kelurahanListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getKit(modulId: String) = scope.launch {
        try {
            kitListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getKit(modulId)
            }
            kitListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kitListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getKitRef() = scope.launch {
        try {
            kitRefListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getKitRefferences()
            }
            kitRefListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kitRefListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun createKit(request: KitRequest) = scope.launch {
        try {
            kitState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).createKit(request)
            }
            kitState.value = BaseState(loading = false, error = null, data = data)
        } catch (e: Exception) {
            kitState.value = BaseState(loading = false, error = e, data = null)
        }
    }


    fun updateKit(id : String, request: UpdateKitRequest) = scope.launch {
        try {
            updateKitState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).updateKit(id, request)
            }
            updateKitState.value = BaseState(loading = false, error = null, data = data)
        } catch (e: Exception) {
            updateKitState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun createSchedule(request: JadwalRequest) = scope.launch {
        try {
            scheduleState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).createSchedule(request)
            }
            scheduleState.value = BaseState(loading = false, error = null, data = data)
        } catch (e: Exception) {
            scheduleState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun updateSchedule(id : String, request: UpdateJadwalRequest) = scope.launch {
        try {
            updateScheduleState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).updateSchedule(id, request)
            }
            updateScheduleState.value = BaseState(loading = false, error = null, data = data)
        } catch (e: Exception) {
            updateScheduleState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getSchedule(modulId: String) = scope.launch {
        try {
            scheduleListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getSchedule(modulId)
            }
            scheduleListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            scheduleListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getService(modulId: String) = scope.launch {
        try {
            serviceListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getService(modulId)
            }
            serviceListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            serviceListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun createService(modulId: String, name: String, foto: File?) = scope.launch {
        try {
            serviceState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                if (foto != null) {
                    PosyanduRepository(getApplication()).createService(
                        modulId,
                        name,
                        foto = foto
                    )
                } else {
                    PosyanduRepository(getApplication()).createService(modulId, name)
                }
            }
            serviceState.value = BaseState(loading = false, error = null, data = data)
        } catch (e: Exception) {
            serviceState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun updateService(id: String, name: String, foto: File?) = scope.launch {
        try {
            updateServiceState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).updateService(
                    idService = id,
                    serviceName = name,
                    foto = foto
                )
            }
            updateServiceState.value = BaseState(loading = false, error = null, data = data)
        } catch (e: Exception) {
            updateServiceState.value = BaseState(loading = false, error = e, data = null)
        }
    }

    fun getKader(modulId: String) = scope.launch {
        try {
            kaderListState.value = BaseState(loading = true)
            val data = withContext(coroutineContext) {
                PosyanduRepository(getApplication()).getKader(modulId)
            }
            kaderListState.value = BaseState(loading = false, error = null, data = data)

        } catch (e: Exception) {
            kaderListState.value = BaseState(loading = false, error = e, data = null)
        }
    }

}