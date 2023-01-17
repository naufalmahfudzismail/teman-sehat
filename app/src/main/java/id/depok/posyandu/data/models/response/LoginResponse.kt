package id.depok.posyandu.data.models.response


import com.google.gson.annotations.SerializedName
import id.depok.posyandu.data.models.Kader
import id.depok.posyandu.data.models.User

data class LoginResponse(
    @SerializedName("kader")
    var kader: Kader? = null,
    @SerializedName("user")
    var user: User? = null
)
