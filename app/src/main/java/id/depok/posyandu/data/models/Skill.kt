package id.depok.posyandu.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Skill {

    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

}