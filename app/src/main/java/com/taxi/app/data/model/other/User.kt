package com.taxi.app.data.model.other

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User : Serializable {

    @SerializedName("Id")
    var id: String = ""

    @SerializedName("Name")
    var name: String = ""

    @SerializedName("Email")
    var email: String = ""

    @SerializedName("Token")
    var token: String = ""

}

