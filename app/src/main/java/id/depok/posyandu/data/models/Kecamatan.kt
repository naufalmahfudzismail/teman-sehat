package id.depok.posyandu.data.models


import com.google.gson.annotations.SerializedName

data class Kecamatan(
    @SerializedName("created_at")
    var createdAt: Any? = null,
    @SerializedName("deleted_at")
    var deletedAt: Any? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("kode_wilayah")
    var kodeWilayah: String? = null,
    @SerializedName("nama")
    var nama: String = "",
    @SerializedName("sk")
    var sk: Any? = null,
    @SerializedName("updated_at")
    var updatedAt: Any? = null
)