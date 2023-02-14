package com.autodhil.tutarisiswa.ui.activity.minggu1

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autodhil.tutarisiswa.databinding.ActivityMinggu1Binding
import com.autodhil.tutarisiswa.model.Tugas
import com.autodhil.tutarisiswa.ui.data.User
import com.autodhil.tutarisiswa.utils.local.SessionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pajaga.utils.other.Constant


class Minggu1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMinggu1Binding

    private lateinit var session: SessionManager

    var username = ""
    var idTugas = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinggu1Binding.inflate(layoutInflater)
        session = SessionManager(this)
        username = intent.getStringExtra("username").toString()
        idTugas = intent.getStringExtra("id").toString()
        initview()

        binding.btnKirim.setOnClickListener {
            if (session.username.equals("admin")) {
                if (!binding.nilai.text.isNullOrEmpty()){
                    kirimGuru()
                }else{
                    Toast.makeText(this, "Nilai Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                }

            } else {
                if (!binding.komentar.text.isNullOrEmpty()){
                    kirimSiswa()
                }else{
                    Toast.makeText(this, "Nilai Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                }
            }

        }
        setContentView(binding.root)
    }

    private fun kirimGuru() {

        val reference =
            FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .reference

        reference
            .child(Constant.dbnode_tugas)
            .child(username)
            .child(idTugas)
            .child("nilai")
            .setValue(binding.nilai.text.toString())
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                binding.btnKirim.isEnabled = false
                binding.nilai.isEnabled = false
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }


    }

    private fun kirimSiswa() {
        val reference =
            FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .reference
        //input Tugas
        val tugas = Tugas(
            1,
            "",
            username,
            "",
            binding.komentar.text.toString(),
            ""
        )
        reference
            .child(Constant.dbnode_tugas)
            .child(username)
            .child(idTugas)
            .setValue(tugas) { databaseError, databaseReference ->
                if (databaseError != null) {
                    Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show()
                    binding.btnKirim.isEnabled = false
                    binding.nilai.isEnabled = false
                } else {
                    Toast.makeText(this, "Data berhasil", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initview() {
        if (session.username.equals("admin")){
            binding.btnKirim.setText("Beri Nilai")
        }
        val reference =
            FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(Constant.dbnode_tugas)
        val referenceTugas = reference.child(username).child(idTugas)
        referenceTugas.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tugas = dataSnapshot.getValue(Tugas::class.java)
                if (!tugas?.comment.isNullOrEmpty()) {
                    binding.komentar.setText(tugas?.comment.toString())
                    binding.komentar.isEnabled = false
                    binding.nilai.visibility = View.VISIBLE


                    if (session.username == "admin") {
                        binding.nilai.isEnabled = true
                        binding.komentar.isEnabled = false
                        binding.btnKirim.isEnabled = true
                        if (!tugas?.nilai.isNullOrEmpty()){
                            binding.nilai.isEnabled = false
                            binding.nilai.setText(tugas?.nilai)
                            binding.btnKirim.isEnabled = false
                        }

                    } else {
                        binding.btnKirim.isEnabled = false
                        binding.nilai.isEnabled = false
                        if (tugas?.nilai.isNullOrEmpty()) {
                            binding.nilai.setText("Belum Dinilai")
                            binding.nilai.isEnabled = false
                        }else{
                            binding.nilai.setText(tugas?.nilai)
                            binding.nilai.isEnabled = false
                        }
                    }
                }else{
                    if (session.username == "admin") {
                        binding.nilai.isEnabled = true
                        binding.komentar.setText("Belum Diisi")
                        binding.komentar.isEnabled = false
                        binding.btnKirim.isEnabled = false
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "onCancelled ", databaseError.toException())
            }
        })

    }

}

