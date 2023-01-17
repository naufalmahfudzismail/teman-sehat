package id.depok.posyandu.data.repositories

import android.content.Context
import id.depok.posyandu.data.models.*
import id.depok.posyandu.data.models.request.*
import id.depok.posyandu.data.models.response.KaderDetailResponse
import id.depok.posyandu.data.models.response.PosyanduDetailResponse
import id.depok.posyandu.data.services.RetrofitFactory
import id.depok.posyandu.utils.extension.toMediaTypeOrNull
import id.depok.posyandu.utils.extension.toRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PosyanduRepository(val context: Context) {

    suspend fun getPosyandu(
        modulId: String
    ): PosyanduDetailResponse? {

        val data =
            RetrofitFactory.getRetrofitService(context).getPosyanduDetailAsync(
                modulId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getPosyandu(
        kecamatanId: String,
        kelurahanId: String
    ): List<Posyandu>? {

        val data =
            RetrofitFactory.getRetrofitService(context).getPosyanduAsync(
                kecamatanId, kelurahanId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun updatePosyandu(
        modulId: String,
        req: PosyanduRequest
    ): Posyandu? {

        val data =
            RetrofitFactory.getRetrofitService(context).updatePosyanduAsync(
                req, modulId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun updatePosyandu(
        modulId: String,
        req: PosyanduRequest,
        file: File
    ): Posyandu? {

        val map = HashMap<String, RequestBody>()

        req.let {
            map["alamat"] =
                it.alamat.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            map["ketua"] =
                it.ketua.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            map["nama"] =
                it.nama.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            map["skl"] = it.skl.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            map["tlp"] = it.tlp.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        val fileRequestBody: RequestBody = RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(),
            file
        )
        val filePart = MultipartBody.Part.createFormData(
            "foto",
            "posyandu-$modulId.jpg",
            fileRequestBody
        )


        val data =
            RetrofitFactory.getRetrofitService(context).updatePosyanduAsync(
                id = modulId, data = map, file = filePart
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getKit(
        modulId: String
    ): List<Kit>? {

        val data =
            RetrofitFactory.getRetrofitService(context).getKitAsync(
                modulId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getKitRefferences(
    ): List<Kit>? {
        val data =
            RetrofitFactory.getRetrofitService(context).getKitReferenceAsync()
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data
        throw Exception(result.code().toString() + "," + result.errorBody()?.string())
    }

    suspend fun createKit(
        request: KitRequest
    ): Kit? {

        val data =
            RetrofitFactory.getRetrofitService(context).createKitAsync(
                request
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun updateKit(
        id: String,
        request: UpdateKitRequest
    ): Kit? {

        val data =
            RetrofitFactory.getRetrofitService(context).updateKitAsync(
                id,
                request
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getSchedule(
        modulId: String
    ): List<Jadwal>? {

        val data =
            RetrofitFactory.getRetrofitService(context).getScheduleAsync(
                modulId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun createSchedule(
        request: JadwalRequest
    ): Jadwal? {

        val data =
            RetrofitFactory.getRetrofitService(context).createScheduleAsync(
                request
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun updateSchedule(
        id: String,
        request: UpdateJadwalRequest
    ): Jadwal? {

        val data =
            RetrofitFactory.getRetrofitService(context).updateScheduleAsync(
                id,
                request
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getService(
        modulId: String
    ): List<Pelayanan>? {

        val data =
            RetrofitFactory.getRetrofitService(context).getServiceAsync(
                modulId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun createService(
        modulId: String,
        serviceName: String,
    ): Pelayanan? {


        val id = modulId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val name = serviceName.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val data =
            RetrofitFactory.getRetrofitService(context).createServiceAsync(
                id, name
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun createService(
        modulId: String,
        serviceName: String,
        foto: File
    ): Pelayanan? {

        val id = modulId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val name = serviceName.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val fileRequestBody: RequestBody = RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(),
            foto
        )
        val filePart = MultipartBody.Part.createFormData(
            "foto",
            "pelayanan-$id-$name.jpg",
            fileRequestBody
        )
        val data =
            RetrofitFactory.getRetrofitService(context).createServiceAsync(
                modulId = id, name = name, file = filePart
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun updateService(
        idService: String,
        serviceName: String,
        foto: File?
    ): Pelayanan? {
        val data = if (foto != null) {
            val name = serviceName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val fileRequestBody: RequestBody = RequestBody.create(
                "image/jpeg".toMediaTypeOrNull(),
                foto
            )
            val filePart = MultipartBody.Part.createFormData(
                "foto",
                "pelayanan-$idService-$name.jpg",
                fileRequestBody
            )
            RetrofitFactory.getRetrofitService(context).updateServiceAsync(
                id = idService,
                name = name,
                file = filePart
            )
        } else {
            RetrofitFactory.getRetrofitService(context).updateServiceAsync(
                id = idService,
                request = UpdatePelayananRequest().apply {
                    this.nama = serviceName
                }
            )
        }

        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getKader(
        modulId: String
    ): List<Kader>? {

        val data =
            RetrofitFactory.getRetrofitService(context).getKaderPosyanduAsync(
                modulId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getKecamatan(
    ): List<Kecamatan>? {
        val data =
            RetrofitFactory.getRetrofitService(context).getKecamatanAsync()
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data
        throw Exception(result.code().toString() + "," + result.errorBody()?.string())
    }

    suspend fun getKelurahan(
        kecamatanId: String
    ): List<Kelurahan>? {

        val data =
            RetrofitFactory.getRetrofitService(context).getKelurahanAsync(kecamatanId)
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

    suspend fun getKelurahan(
    ): List<Kelurahan>? {

        val data =
            RetrofitFactory.getRetrofitService(context).getKelurahanAsync()
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }
}