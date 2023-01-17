package id.depok.posyandu.data.models.request


import com.google.gson.annotations.SerializedName

data class UpdateJadwalRequest(
    @SerializedName("hari")
    var hari: String? = null,
    @SerializedName("jam_operasional")
    var jamOperasional: String? = null,
    @SerializedName("nama_kegiatan")
    var namaKegiatan: String? = null,
    @SerializedName("ket")
    var ket: String = ""
)