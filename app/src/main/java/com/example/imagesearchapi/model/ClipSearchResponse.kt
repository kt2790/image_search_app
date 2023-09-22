package com.example.imagesearchapi.model

import com.google.gson.annotations.SerializedName

data class ClipSearchResponse(
    @SerializedName("meta")
    val metaData: MetaData?,

    @SerializedName("documents")
    var documents: MutableList<KakaoClip>?
)