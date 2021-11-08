package com.example.gitget.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepositoryOwner(
    @SerializedName("login")
    val userName: String
): Parcelable