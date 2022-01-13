package com.taxi.app.data.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginRequest : Serializable {

    @SerializedName("email")
    var email: String = ""

    @SerializedName("password")
    var password: String = ""

}