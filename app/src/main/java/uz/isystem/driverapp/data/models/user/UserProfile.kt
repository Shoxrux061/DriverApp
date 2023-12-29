package uz.isystem.driverapp.data.models.user

data class UserProfile(
    val name : String? = "",
    val lastName : String? = "",
    val email : String? = "",
    val phone : String? = "",
    val city : String? = ""
)
