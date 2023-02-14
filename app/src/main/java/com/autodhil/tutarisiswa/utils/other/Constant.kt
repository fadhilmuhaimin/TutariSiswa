package com.pajaga.utils.other

import com.autodhil.tutarisiswa.model.Tugas

object Constant {




    // mime type
    const val MIME_ALL_IMAGE = "image/*"
    const val MIME_ALL_DOCUMENT = "application/*"
    const val MIME_PDF = "application/pdf"

    // saved data
    const val examplesKeySavedDataString = "examples"
    const val examplesKeySavedDataObject = "examples"

    const val KEY_TOKEN = "token"
    const val ONACTIVED_KEY_VOLUME = "key volume"
    const val SUM_PLUS = "sum plus"

    // KEY location
    const val KEY_LATITUDE = "latitude"
    const val KEY_LONGITUDE = "longitude"

    // Notif
    const val TOPIC = "/topics/testing"
    const val CHANNEL_ID = "my_channel"

    // retrofit base url
    const val BASE_URL = "http://192.168.1.16:4000/"

    // list dropdown examples
    val exampleListDropwdownText = listOf("dropdown 1", "dropdown 2")

    // fcm
    const val BASE_URL_FCM = "https://fcm.googleapis.com"
    const val SERVER_KEY = "AAAACTyz42o:APA91bHtV4T7lNBXIOdr4M0cCmi71p1jQzxvhsaudq0LAQBGxIN12YR6Iy8nlxUO9VXEiMQvMx29rO5mFadHS3lKYbQbilSH1SMAaP9_EsCiZttxBAAEP9QFg0gmOC_W3CS1E2RzSr7o"
    const val CONTENT_TYPE = "application/json"

    // news
    const val BASE_URL_NEWS = "https://newsdata.io"
    const val BASE_URL_NEWS_API = "https://newsapi.org"
    const val API_KEY_NEWS = "pub_576787425b621b5b303a8af432758da02525"


    //Db Tugas

    var dbnode_tugas = "tugas"
    var dbnode_user = "user"

    val listMinggu2 = arrayListOf(
        Tugas(2, "Latihan 1", "", "", ""),
        Tugas(3, "Latihan 2", "", "", ""),
        Tugas(4, "Latihan 3", "", "", ""),
        Tugas(5, "Latihan 4", "", "", ""),
        Tugas(6, "Latihan 5", "", "", ""),
        Tugas(7, "Latihan 6", "", "", "")

//        Tugas(8, "Latihan 7", "", "", ""),
    )
    val listMinggu3 = arrayListOf(
        Tugas(8, "Uji Coba 1", "", "", ""),
        Tugas(9, "Uji Coba 2", "", "", "")

//        Tugas(8, "Latihan 7", "", "", ""),
    )



}