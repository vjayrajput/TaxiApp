package com.taxi.app.data.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LogoutRequest : Serializable {

    @SerializedName("token")
    var token: String = ""

}