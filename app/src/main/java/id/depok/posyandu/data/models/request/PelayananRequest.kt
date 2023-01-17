package id.depok.posyandu.data.models.request


import com.google.gson.annotations.SerializedName

data class PelayananRequest(
    @SerializedName("foto")
    var foto: Any? = null,
    @SerializedName("nama")
    var nama: String? = null,
    @SerializedName("modul_id")
    var modulId: String? = null
)