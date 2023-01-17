package id.depok.posyandu.data.repositories

import android.content.Context
import id.depok.posyandu.data.models.Kader
import id.depok.posyandu.data.models.Skill
import id.depok.posyandu.data.models.request.KaderRequest
import id.depok.posyandu.data.models.request.SkillRequest
import id.depok.posyandu.data.models.response.KaderDetailResponse
import id.depok.posyandu.data.models.response.LoginResponse
import id.depok.posyandu.data.services.RetrofitFactory

class KaderRepository(val context: Context) {

    suspend fun getUserKader(
        userId: String
    ): KaderDetailResponse? {

        val data =
            RetrofitFactory.getRetrofitService(context).getUserKaderAsync(
                userId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())
    }

    suspend fun createSkillKader(
        req : SkillRequest
    ): Skill? {

        val data =
            RetrofitFactory.getRetrofitService(context).createSkillAsync(
                req
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())
    }

    suspend fun getKader(
        kaderId: String
    ): KaderDetailResponse? {

        val data =
            RetrofitFactory.getRetrofitService(context).getKaderAsync(
                kaderId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }


    suspend fun updateKader(
        kaderId: String,
        req : KaderRequest
    ): Kader? {

        val data =
            RetrofitFactory.getRetrofitService(context).updateKaderAsync(
                req, kaderId
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

}