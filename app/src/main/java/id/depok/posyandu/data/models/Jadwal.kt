package id.depok.posyandu.data.models


import com.google.gson.annotations.SerializedName

data class Jadwal(
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("deleted_at")
    var deletedAt: Any? = null,
    @SerializedName("hari")
    var hari: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("jam_operasional")
    var jamOperasional: String? = null,
    @SerializedName("ket")
    var ket: String? = null,
    @SerializedName("nama_kegiatan")
    var namaKegiatan: String? = null,
    @SerializedName("modul_id")
    var modulId: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null
)