package id.depok.posyandu.data.models.request


import com.google.gson.annotations.SerializedName

data class PjbRequest(
    @SerializedName("no_kk")
    var noKK: String? = "",
    @SerializedName("kader_id")
    var kaderId: String? = null,
   /* @SerializedName("keluarga_id")
    var keluargaId: String? = null,*/
    @SerializedName("status_jentik_id")
    var statusJentikId: String? = null
)