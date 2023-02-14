package com.autodhil.tutarisiswa.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.autodhil.tutarisiswa.R
import com.autodhil.tutarisiswa.databinding.FragmentBaseBinding
import com.autodhil.tutarisiswa.utils.system.MainPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class BaseFragment : Fragment() {
    private lateinit var viewPagerr: ViewPager
    var exit = false

    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var binding: FragmentBaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseBinding.inflate(inflater,container,false)
        viewPagerr = binding.fragmentOcntainer
        bottomNavBar = binding.bottomNav


        setViewPagerAdapter()
        setBottomNavigation()
        setViewPagerListener()

        return binding.root
    }



    private fun setViewPagerListener() {
        viewPagerr.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> bottomNavBar.selectedItemId = R.id.menu_home
                    1 -> bottomNavBar.selectedItemId = R.id.menu_tugas
                    2 -> bottomNavBar.selectedItemId = R.id.menu_profil
                }
            }

        })
    }

    private fun setViewPagerAdapter() {
        viewPagerr.adapter = MainPagerAdapter(childFragmentManager)
    }

    private fun setBottomNavigation() {
        bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> viewPagerr.currentItem = 0
                R.id.menu_tugas -> viewPagerr.currentItem = 1
                R.id.menu_profil -> viewPagerr.currentItem = 2
            }
            return@setOnNavigationItemSelectedListener true
        }

    }


}