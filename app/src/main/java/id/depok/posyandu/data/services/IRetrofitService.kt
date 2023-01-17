package id.depok.posyandu.data.services

import id.depok.posyandu.base.BaseListResponse
import id.depok.posyandu.base.BaseResponse
import id.depok.posyandu.data.models.*
import id.depok.posyandu.data.models.request.*
import id.depok.posyandu.data.models.response.KaderDetailResponse
import id.depok.posyandu.data.models.response.LoginResponse
import id.depok.posyandu.data.models.response.PosyanduDetailResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface IRetrofitService {

    @GET("get-kecamatan")
    fun getKecamatanAsync(): Deferred<Response<BaseListResponse<Kecamatan>>>

    @POST("login")
    fun loginAsync(
        @Query("email") customerName: String,
        @Query("password") idCard: String,
    ): Deferred<Response<BaseResponse<LoginResponse>>>

    @GET("get-kelurahan/kecamatan/{id}")
    fun getKelurahanAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseListResponse<Kelurahan>>>

    @GET("get-kelurahan")
    fun getKelurahanAsync(
    ): Deferred<Response<BaseListResponse<Kelurahan>>>

    @GET("get-kader/{id}")
    fun getKaderAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseResponse<KaderDetailResponse>>>

    @PUT("kader/update/{id}")
    fun updateKaderAsync(
        @Body request: KaderRequest,
        @Path("id") id: String,
    ): Deferred<Response<BaseResponse<Kader>>>

    @GET("get-user-kader/{id}")
    fun getUserKaderAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseResponse<KaderDetailResponse>>>

    @GET("get-posyandu/filter")
    fun getPosyanduAsync(
        @Query("kecamatan") kecamatan: String,
        @Query("kelurahan") kelurahan: String,
    ): Deferred<Response<BaseListResponse<Posyandu>>>

    @GET("get-posyandu/detail/{id}")
    fun getPosyanduDetailAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseResponse<PosyanduDetailResponse>>>

    @PUT("posyandu/update/{id}")
    fun updatePosyanduAsync(
        @Body request: PosyanduRequest,
        @Path("id") id: String
    ): Deferred<Response<BaseResponse<Posyandu>>>

    @Multipart
    @POST("posyandu/update-image/{id}")
    fun updatePosyanduAsync(
        @Path("id") id: String,
        @PartMap data: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part,
    ): Deferred<Response<BaseResponse<Posyandu>>>

    @POST("create-pjb")
    fun createPjbAsync(
        @Body request: PjbRequest
    ): Deferred<Response<BaseResponse<PjbRequest>>>

    @GET("keluarga/{kk}")
    fun getKeluargaAsync(
        @Path("kk") id: String,
    ): Deferred<Response<BaseResponse<Keluarga>>>
    @GET("kit")
    fun getKitReferenceAsync(
    ): Deferred<Response<BaseListResponse<Kit>>>

    @GET("stok-kit/{id}")
    fun getKitAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseListResponse<Kit>>>

    @POST("stok-kit")
    fun createKitAsync(
        @Body request: KitRequest
    ): Deferred<Response<BaseResponse<Kit>>>

    @PUT("stok-kit/{id}")
    fun updateKitAsync(
        @Path("id") id: String,
        @Body request: UpdateKitRequest
    ): Deferred<Response<BaseResponse<Kit>>>

    @GET("jadwal/{id}")
    fun getScheduleAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseListResponse<Jadwal>>>

    @POST("jadwal")
    fun createScheduleAsync(
        @Body request: JadwalRequest
    ): Deferred<Response<BaseResponse<Jadwal>>>

    @PUT("jadwal/{id}")
    fun updateScheduleAsync(
        @Path("id") id: String,
        @Body request: UpdateJadwalRequest
    ): Deferred<Response<BaseResponse<Jadwal>>>

    @POST("skill")
    fun createSkillAsync(
        @Body request: SkillRequest
    ): Deferred<Response<BaseResponse<Skill>>>

    @GET("pelayanan/{id}")
    fun getServiceAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseListResponse<Pelayanan>>>

    @Multipart
    @POST("pelayanan")
    fun createServiceAsync(
        @Part("modul_id") modulId: RequestBody,
        @Part("nama") name: RequestBody,
        @Part file: MultipartBody.Part,
    ): Deferred<Response<BaseResponse<Pelayanan>>>

    @PUT("pelayanan/{id}")
    fun updateServiceAsync(
        @Path("id") id: String,
        @Body request: UpdatePelayananRequest
    ): Deferred<Response<BaseResponse<Pelayanan>>>

    @Multipart
    @POST("pelayanan/update-image/{id}")
    fun updateServiceAsync(
        @Path("id") id: String,
        @Part("nama") name: RequestBody,
        @Part file: MultipartBody.Part,
    ): Deferred<Response<BaseResponse<Pelayanan>>>

    @Multipart
    @POST("pelayanan")
    fun createServiceAsync(
        @Part("modul_id") modulId: RequestBody,
        @Part("nama") name: RequestBody,
    ): Deferred<Response<BaseResponse<Pelayanan>>>

    @POST("pelayanan")
    fun createServiceAsync(
        @Body request: PelayananRequest
    ): Deferred<Response<BaseResponse<Pelayanan>>>

    @GET("get-kader-posyandu/{id}")
    fun getKaderPosyanduAsync(
        @Path("id") id: String,
    ): Deferred<Response<BaseListResponse<Kader>>>


}