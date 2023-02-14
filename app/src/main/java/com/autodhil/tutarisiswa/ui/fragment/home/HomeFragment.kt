package com.autodhil.tutarisiswa.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.autodhil.tutarisiswa.R
import com.autodhil.tutarisiswa.databinding.FragmentHomeBinding
import com.autodhil.tutarisiswa.utils.local.SessionManager
import com.pajaga.utils.system.moveNavigationTo


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var session : SessionManager

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.latihan.setOnClickListener {
            view?.let { it1 -> moveNavigationTo(it1,R.id.latihanFragment) }
        }
        session = context?.let { SessionManager(it) }!!
        binding.text.setText("Selamat Datang, \n ${session.name}")

        return binding.root
    }


}