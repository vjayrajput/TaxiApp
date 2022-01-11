package com.taxi.app.data.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

abstract class BaseResponse : Serializable {

    @SerializedName("code")
    var code: Int = -1

    @SerializedName("status")
    var status: Boolean = false

    @SerializedName("message")
    var message = ""


    override fun toString(): String {
        return "BaseResponse(code=$code, status=$status, message='$message')"
    }

}
