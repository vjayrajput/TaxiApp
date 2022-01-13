package com.taxi.app.data.model.api

import com.google.gson.annotations.SerializedName
import com.taxi.app.data.model.other.User
import java.io.Serializable

class RegisterResponse : BaseResponse(), Serializable {

    @SerializedName("data")
    var userData: User = User()

}

