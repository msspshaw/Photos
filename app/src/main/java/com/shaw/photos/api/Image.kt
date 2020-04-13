package com.shaw.photos.api

import java.math.BigInteger

class Image (
    val id: String,
    val author: String,
    val width: BigInteger,
    val height: BigInteger,
    val url: String,
    val download_url: String,
    var totalSize: BigInteger
)