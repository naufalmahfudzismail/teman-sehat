package id.depok.posyandu.data.models.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.depok.posyandu.data.models.*

class PosyanduDetailResponse(

) {
    @SerializedName("jadwal")
    @Expose
    var jadwal: List<Jadwal> = arrayListOf()

    @SerializedName("kader")
    @Expose
    var kader: List<Kader> = arrayListOf()

    @SerializedName("kit")
    @Expose
    var kit: List<Kit> = arrayListOf()

    @SerializedName("pelayanan")
    @Expose
    var pelayanan: List<Pelayanan> = arrayListOf()

    @SerializedName("posyandu")
    @Expose
    var posyandu: Posyandu? = null
}