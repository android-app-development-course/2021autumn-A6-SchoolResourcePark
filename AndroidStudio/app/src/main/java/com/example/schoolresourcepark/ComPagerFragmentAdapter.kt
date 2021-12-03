package com.example.schoolresourcepark

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ComPagerFragmentAdapter (var compagerfragmentList : List<Fragment>, fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!){
    override fun getItem(position: Int): Fragment {
        return compagerfragmentList[position]
    }

    override fun getCount(): Int {
        return compagerfragmentList.size
    }
}