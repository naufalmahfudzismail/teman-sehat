package id.depok.posyandu.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("created_at")
    var createdAt: Any? = null,
    @SerializedName("deleted_at")
    var deletedAt: Any? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("role")
    var role: String? = null,
    @SerializedName("updated_at")
    var updatedAt: Any? = null,
    @SerializedName("username")
    var username: String? = null
)