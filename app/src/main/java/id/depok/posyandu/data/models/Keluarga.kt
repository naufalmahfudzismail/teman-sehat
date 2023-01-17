package id.depok.posyandu.data.models


import com.google.gson.annotations.SerializedName

data class Keluarga(
    @SerializedName("alamat")
    var alamat: String? = null,
    @SerializedName("created_at")
    var createdAt: Any? = null,
    @SerializedName("deleted_at")
    var deletedAt: Any? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("no_kk")
    var noKk: String? = null,
    @SerializedName("updated_at")
    var updatedAt: Any? = null
)