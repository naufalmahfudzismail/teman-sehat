package id.depok.posyandu.data.models.request


import com.google.gson.annotations.SerializedName

data class UpdateKitRequest(
    @SerializedName("jumlah_stok")
    var jumlahStok: String = ""
)