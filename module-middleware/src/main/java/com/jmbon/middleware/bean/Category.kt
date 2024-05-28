package com.jmbon.middleware.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class Category(
    @SerializedName("categorys")
    var categorys: ArrayList<CategoryList> = arrayListOf(),
    @SerializedName("data")
    var data: Categorys = Categorys(),
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CategoryList(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("is_seleted")
    var isSelected: Boolean = false
) : Parcelable

/**
 * 就很
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Categorys(
    @SerializedName("categorys")
    var categorys: ArrayList<CategoryList> = arrayListOf(),
    ) : Parcelable

