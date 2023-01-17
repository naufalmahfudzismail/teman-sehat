package id.depok.posyandu.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseListResponse<T> {

    @SerializedName("data")
    @Expose
    var data: List<T> = arrayListOf()

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("error")
    @Expose
    var error: Boolean? = null
}