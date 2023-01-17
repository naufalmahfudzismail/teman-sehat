package id.depok.posyandu.data.models.request


import com.google.gson.annotations.SerializedName

data class KitRequest(
    @SerializedName("jumlah_stok")
    var jumlahStok: String? = null,
    @SerializedName("kit_modul_id")
    var kitPosyanduId: String? = null,
    @SerializedName("modul_id")
    var modulId: String? = null
)