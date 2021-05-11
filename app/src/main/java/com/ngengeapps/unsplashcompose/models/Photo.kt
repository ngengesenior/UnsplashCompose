package com.ngengeapps.unsplashcompose.models

data class Photo(
    val id:String,
    val urls:Urls,
    val likes:Int,
    val user: User
)

data class Urls(
    val regular:String,
    val full:String,
)

data class User(
    val id:String,
    val username:String,
    val name:String,
    val profile_image:ProfileImage

)
data class ProfileImage(
    val small:String,
    val medium:String
)

val sampleUser = User(
    id = "24ivlccTYiE",
    username = "ngengesenior",
    name = "Ngenge Senior",
    profile_image = ProfileImage(small = "https://images.unsplash.com/profile-1511478485908-f4c63df153f9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",medium = "https://images.unsplash.com/profile-1511478485908-f4c63df153f9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64")
)
