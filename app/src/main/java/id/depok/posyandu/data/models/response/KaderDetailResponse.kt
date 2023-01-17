package id.depok.posyandu.data.models.response


import com.google.gson.annotations.SerializedName
import id.depok.posyandu.data.models.Kader
import id.depok.posyandu.data.models.Posyandu
import id.depok.posyandu.data.models.Skill

data class KaderDetailResponse(
    @SerializedName("kader")
    var kader: Kader? = null,
    @SerializedName("posyandu")
    var posyandu: Posyandu? = null,
    @SerializedName("skill")
    var skill: List<Skill> = arrayListOf()
)