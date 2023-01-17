package id.depok.posyandu.data.repositories

import android.content.Context
import id.depok.posyandu.data.models.Keluarga
import id.depok.posyandu.data.models.Skill
import id.depok.posyandu.data.models.request.PjbRequest
import id.depok.posyandu.data.models.request.SkillRequest
import id.depok.posyandu.data.services.RetrofitFactory

class PjbRepository(val context : Context) {
    suspend fun createPJB(
        req : PjbRequest
    ): PjbRequest? {

        val data =
            RetrofitFactory.getRetrofitService(context).createPjbAsync(
                req
            )
        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())
    }

    suspend fun getKeluarga(
        kk : String
    ): Keluarga? {

        val data =
            RetrofitFactory.getRetrofitService(context).getKeluargaAsync(kk)

        val result = data.await()
        if (result.isSuccessful) return result.body()?.data

        throw Exception(result.code().toString() + "," + result.errorBody()?.string())
    }
}