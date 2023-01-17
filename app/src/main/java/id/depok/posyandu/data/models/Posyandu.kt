package id.depok.posyandu.data.models

import com.google.gson.annotations.SerializedName

data class Posyandu(
    @SerializedName("alamat")
    var address: String? = null,
    @SerializedName("created_at")
    var creation: String? = null,
    @SerializedName("deleted_at")
    var deletedAt: Any? = null,
    @SerializedName("foto")
    var foto: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("kecamatan")
    var district: String? = null,
    @SerializedName("kecamatan_id")
    var districtId: String? = null,
    @SerializedName("strata")
    var strata: String? = null,
    @SerializedName("kelurahan")
    var subDistrict: String? = null,
    @SerializedName("kelurahan_id")
    var subDistrictId: String? = null,
    @SerializedName("ketua")
    var leader: String? = null,
    @SerializedName("koordinat")
    var koordinat: String? = null,
    @SerializedName("nama")
    var name: String? = null,
    @SerializedName("sk")
    var sk: String? = null,
    @SerializedName("skl")
    var skl: String? = null,
    @SerializedName("tlp")
    var tlp: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null
)
