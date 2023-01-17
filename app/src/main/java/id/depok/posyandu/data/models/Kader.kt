package id.depok.posyandu.data.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Kader {
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("nama_kader")
    @Expose
    var namaKader: String? = null

    @SerializedName("nik")
    @Expose
    var nik: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("no_kader")
    @Expose
    var noKader: String? = null

    @SerializedName("modul_id")
    @Expose
    var modulId: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("skill")
    @Expose
    var skills: List<Skill> = arrayListOf()
}