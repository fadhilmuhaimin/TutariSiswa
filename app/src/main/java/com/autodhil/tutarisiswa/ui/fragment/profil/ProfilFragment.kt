package com.autodhil.tutarisiswa.ui.fragment.profil

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.autodhil.tutarisiswa.R
import com.autodhil.tutarisiswa.databinding.FragmentProfilBinding
import com.autodhil.tutarisiswa.model.Tugas
import com.autodhil.tutarisiswa.ui.CobaActivity
import com.autodhil.tutarisiswa.ui.data.User
import com.autodhil.tutarisiswa.utils.local.SessionManager
import com.google.firebase.database.FirebaseDatabase
import com.pajaga.utils.other.Constant


class ProfilFragment : Fragment() {

    private lateinit var binding : FragmentProfilBinding

    companion object {
        fun newInstance(): ProfilFragment {
            return ProfilFragment()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val session = SessionManager(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater,container,false)


        binding.upload.setOnClickListener {
            startActivity(Intent(context,CobaActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            session.logoutUser()
        }
        

        binding.upload.setOnClickListener {
            Toast.makeText(context, "adalah", Toast.LENGTH_SHORT).show()
            val reference = FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
            //input Tugas


            val user = User(
                "444",
                "Nama User 4"
            )

                reference
                    .child(Constant.dbnode_user)
                    .child(user.username)
                    .setValue(user)


        }
        return binding.root
    }
}