package id.depok.posyandu.data.models


import com.google.gson.annotations.SerializedName

data class Pelayanan(
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("deleted_at")
    var deletedAt: Any? = null,
    @SerializedName("foto")
    var foto: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("nama")
    var nama: String? = null,
    @SerializedName("modul_id")
    var modulId: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null
)