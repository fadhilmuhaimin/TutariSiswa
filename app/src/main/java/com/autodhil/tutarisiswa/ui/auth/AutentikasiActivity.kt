package com.autodhil.tutarisiswa.ui.auth

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autodhil.tutarisiswa.MainActivity
import com.autodhil.tutarisiswa.databinding.ActivityAutentikasiBinding
import com.autodhil.tutarisiswa.ui.data.User
import com.autodhil.tutarisiswa.utils.local.SessionManager
import com.google.firebase.database.*
import com.pajaga.utils.other.Constant
import java.util.*


class AutentikasiActivity : AppCompatActivity() {
    private var username: String? = null
    private lateinit var binding: ActivityAutentikasiBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutentikasiBinding.inflate(layoutInflater)
        binding.masuk.setOnClickListener {
            username = binding.username.text.toString()
            if (!username.isNullOrEmpty()) {
                binding.prgBarMovies.visibility = View.VISIBLE
                initfirebaseLoginUser(username)
            } else {
                Toast.makeText(this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }
        }
        setContentView(binding.root)
    }

    private fun initfirebaseLoginUser(username: String?) {
        val reference =
            FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    Constant.dbnode_user
                )
        val check = username?.let { reference.child(it) }

        check?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    movePage(user)

                    Log.d(TAG, "onDataChange: adakah $user")
                } else {
                    binding.prgBarMovies.visibility = View.GONE
                    Toast.makeText(
                        this@AutentikasiActivity,
                        "Data Tidak Ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun movePage(user: User?) {
        val session  = SessionManager(this)
        user?.let { session.setUsername(it.username) }
        user?.let { session.setName(it.name) }
        session.createLoginSession()
        startActivity(Intent(this,MainActivity::class.java))
    }

}