package com.autodhil.tutarisiswa.ui.fragment.tugas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.autodhil.tutarisiswa.R
import com.autodhil.tutarisiswa.databinding.FragmentTugasBinding
import com.autodhil.tutarisiswa.ui.activity.minggu1.Minggu1Activity
import com.autodhil.tutarisiswa.ui.data.User
import com.autodhil.tutarisiswa.utils.local.SessionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pajaga.utils.other.Constant


@UnstableApi class TugasFragment : Fragment() {

    private lateinit var binding : FragmentTugasBinding
    var list = ArrayList<String>()
    var listUsernameSiswa = ArrayList<String>()

    private lateinit var session : SessionManager

    companion object {
        fun newInstance(): TugasFragment {
            return TugasFragment()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        session = SessionManager(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentTugasBinding.inflate(inflater,container,false)



        binding.itemMinggu1.setOnClickListener {
            if (session.username == "admin"){
                if (session.usernameSiswa.isNullOrEmpty()){
                    Toast.makeText(context, "Silahkan Pilih Siswa Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(context, Minggu1Activity::class.java)
                        .putExtra("id","1")
                        .putExtra("username",session.usernameSiswa))
                }


            }else{
                startActivity(Intent(context, Minggu1Activity::class.java)
                    .putExtra("id","1")
                    .putExtra("username",session.username))
            }

        }

        if (session.username == "admin"){
            binding.textEnd.visibility = View.VISIBLE
            binding.dropdownMonthh.visibility = View.VISIBLE
            initDropdown()
        }



        setListMinggu2()
        setListMinggu3()
        return binding.root
    }

    private fun initDropdown() {
        if (!session.usernameSiswa.isNullOrEmpty()){
            binding.dropdownMonthh.editText?.setText(session.nameSiswa)
        }

        val reference =
            FirebaseDatabase.getInstance("https://tutari-cfcf6-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(
                    Constant.dbnode_user
                )
        reference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for ( postSnaphot in snapshot.children){
                    val user = postSnaphot.getValue(User::class.java)
                    if (user?.username != "admin"){
                        user?.name?.let { list.add(it) }
                        user?.username?.let { listUsernameSiswa.add(it) }
                    }

                }
                setListDropdown(list,listUsernameSiswa)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun setListDropdown(
        list: java.util.ArrayList<String>,
        listUsernameSiswa: ArrayList<String>
    ) {
        (binding.dropdownMonthh.editText as? AutoCompleteTextView)?.apply {
//            if (session.usernameSiswa.isNullOrEmpty()){
//
//            }
            hint = "Pilih Siswa"
            setAdapter(ArrayAdapter(this.context, R.layout.item_dropdown, list))
            setOnItemClickListener { adapterView, view, i, l ->
                session.setUsernameSiswa(listUsernameSiswa.get(i))
                session.setNameSiswa(adapterView.getItemAtPosition(i).toString())
//                Toast.makeText(this.context, "${session.usernameSiswa} ", Toast.LENGTH_SHORT).show()
//                setData(list_id.get(i))
            }
        }
    }

    private fun setListMinggu3() {
        val adapterr = context?.let { TugasAdapter(Constant.listMinggu3, it) }
        binding.rvMinggu3.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterr
        }
    }


    private fun setListMinggu2() {
        val adapterr = context?.let { TugasAdapter(Constant.listMinggu2, it) }
        binding.rvMinggu2.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterr
        }
    }

    override fun onDestroy() {
        session.setUsernameSiswa(null)
        super.onDestroy()
    }

//    override fun onPause() {
//        session.setUsernameSiswa(null)
//        super.onPause()
//    }
//
//    override fun onStop() {
//        session.setUsernameSiswa(null)
//        super.onStop()
//    }
}