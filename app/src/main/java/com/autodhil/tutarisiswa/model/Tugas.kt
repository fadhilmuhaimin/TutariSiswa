package com.autodhil.tutarisiswa.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tugas (
    var idTugas : Int = 0,
    var title : String = "",
    var username : String = "",
    var uriVideo : String = "",
    var comment : String = "",
    var nilai : String = ""
) : Parcelable