package id.depok.posyandu.data.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SkillRequest {

    @SerializedName("kader_id")
    @Expose
    var kaderId: String = ""

    @SerializedName("nama")
    @Expose
    var nama: String = ""

    @SerializedName("status")
    @Expose
    var status: String = ""

}