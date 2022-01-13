package com.taxi.app.data.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RegisterRequest : Serializable {

    @SerializedName("name")
    var name: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("password")
    var password: String = ""

}