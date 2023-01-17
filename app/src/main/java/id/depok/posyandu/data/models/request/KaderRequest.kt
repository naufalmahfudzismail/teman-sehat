package id.depok.posyandu.data.models.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KaderRequest {
  
    @SerializedName("nama_kader")
    @Expose
    var namaKader: String = ""

    @SerializedName("nik")
    @Expose
    var nik: String = ""

    @SerializedName("no_kader")
    @Expose
    var noKader: String = ""

    @SerializedName("password")
    @Expose
    var password: String = ""
}