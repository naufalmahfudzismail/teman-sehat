package id.depok.posyandu.data.models


import com.google.gson.annotations.SerializedName

data class Kit(
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("deleted_at")
    var deletedAt: Any? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("jumlah_stok")
    var jumlahStok: String? = null,
    @SerializedName("kit_modul_id")
    var kitPosyanduId: String? = null,
    @SerializedName("nama_kit")
    var namaKit: String? = null,
    @SerializedName("nama")
    var nama: String? = null,
    @SerializedName("modul_id")
    var modulId: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null
)