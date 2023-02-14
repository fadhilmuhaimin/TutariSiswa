package com.autodhil.tutarisiswa.utils.system

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.autodhil.tutarisiswa.ui.fragment.home.HomeFragment
import com.autodhil.tutarisiswa.ui.fragment.profil.ProfilFragment
import com.autodhil.tutarisiswa.ui.fragment.tugas.TugasFragment

@Suppress("DEPRECATION")
class MainPagerAdapter(var fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return  3
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment.newInstance()
            1 -> TugasFragment.newInstance()
            2 -> ProfilFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }

}