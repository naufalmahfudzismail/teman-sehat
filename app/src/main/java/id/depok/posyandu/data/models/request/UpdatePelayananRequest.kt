package id.depok.posyandu.data.models.request


import com.google.gson.annotations.SerializedName

data class UpdatePelayananRequest(
    @SerializedName("nama")
    var nama: String = ""
)