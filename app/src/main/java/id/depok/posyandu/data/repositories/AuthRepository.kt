package id.depok.posyandu.data.repositories

import android.content.Context
import id.depok.posyandu.data.models.response.LoginResponse
import id.depok.posyandu.data.services.RetrofitFactory

class AuthRepository(val context: Context) {

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse? {
        val data =
            RetrofitFactory.getRetrofitService(context).loginAsync(
                email, password
            )
        val result = data.await()
        if (result.isSuccessful) {
          if(result.body()?.error == true){
              throw Exception(result.body()?.message)
          } else {
              return result.body()?.data
          }
        }
        throw Exception(result.code().toString() + "," + result.errorBody()?.string())

    }

}