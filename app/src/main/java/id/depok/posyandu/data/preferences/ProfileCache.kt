package id.depok.posyandu.data.preferences

import android.content.Context
import com.google.gson.Gson
import id.depok.posyandu.data.models.Kader
import id.depok.posyandu.data.models.response.KaderDetailResponse

class ProfileCache(private val context: Context) {

    companion object {
        const val PROFILE_DATA = "profile_data"
    }

    fun saveProfile(profile: KaderDetailResponse) {
        val gson = Gson()
        val str = gson.toJson(profile)

        AppPreference.saveString(context, PROFILE_DATA, str)
    }

    fun getSavedProfile(): KaderDetailResponse? {
        val profile: KaderDetailResponse
        val gson = Gson()
        val str = AppPreference.getString(context, PROFILE_DATA)
        return if (str != "") {
            profile = gson.fromJson(str,KaderDetailResponse::class.java)
            if(profile.kader == null){
                null
            } else {
                profile
            }
        } else {
            null
        }
    }

    fun clearData() {
        AppPreference.saveString(context, PROFILE_DATA, "")
    }
}