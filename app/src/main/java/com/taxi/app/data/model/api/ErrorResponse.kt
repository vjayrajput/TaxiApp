package com.taxi.app.data.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ErrorResponse : BaseResponse(), Serializable {

    @SerializedName("error")
    var errorMessage = ""

}
