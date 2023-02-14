package com.autodhil.tutarisiswa.utils

import android.content.ContentValues.TAG
import android.util.Log
import com.autodhil.tutarisiswa.model.Song
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

object Repository {


    fun addMusicToDatabase(){
        val databaseMusics = FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("tes")
        val song = mutableListOf<Song?>()

        FirebaseStorage
            .getInstance("gs://tutari-cfcf6.appspot.com")
            .reference
            .child("musics")
            .listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach{item ->
                    item.downloadUrl
                        .addOnSuccessListener { uri ->
                            val names = item.name.split("_")
                            val artistName = names[0].trim()
                            val albumName = names[1].trim()
                            val songName = names[2].trim()
                            val yearAlbum = names[3].trim().replace(".mp3","")

                            val keySong = databaseMusics.push().key
                            song.add(Song(
                                keySong = keySong,
                                nameSong = songName,
                                uriSong = uri.toString(),
                                artistSong = artistName,
                                albumNameSong = albumName,
                                yearSong = yearAlbum.toInt()
                            ))
                            databaseMusics.setValue(song)

                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "addMusicToDatabase:  ${e.message} ")
                Log.e(TAG, "addMusicToDatabase:  ${e.message} ")
            }

    }
}