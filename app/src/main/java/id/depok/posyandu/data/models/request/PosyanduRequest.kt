package id.depok.posyandu.data.models.request


import com.google.gson.annotations.SerializedName

data class PosyanduRequest(
    @SerializedName("alamat")
    var alamat: String? = null,
    @SerializedName("foto")
    var foto: String? = null,
    @SerializedName("ketua")
    var ketua: String? = null,
    @SerializedName("nama")
    var nama: String? = null,
    @SerializedName("sk")
    var sk: String? = null,
    @SerializedName("skl")
    var skl: String? = null,
    @SerializedName("tlp")
    var tlp: String? = null
)