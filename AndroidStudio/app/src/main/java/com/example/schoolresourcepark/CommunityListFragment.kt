package com.example.schoolresourcepark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_community_list.*
import kotlinx.android.synthetic.main.title.*
import java.util.ArrayList



class CommunityListFragment:Fragment() {
    private val titleList = ArrayList<String>()
    private val fragmentList = ArrayList<Fragment>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_community_list,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(activity != null){
            val mainActivity=activity as CommunityAndMine
            val sonfragmentManager=mainActivity.supportFragmentManager
            val sonActionBar=mainActivity.supportActionBar
            sonActionBar?.hide()
            titleText.setText("社区")
            titleList.clear();
            titleList.add("推荐");
            titleList.add("我的");
            fragmentList.clear()
            fragmentList.add(ComListFragmentg1())
            fragmentList.add(ComListFragmentg1())

            val PageAdapter: FragmentPagerAdapter =
                object : FragmentPagerAdapter(sonfragmentManager) {
                    override fun getItem(i: Int): Fragment {
                        return fragmentList[i]
                    }

                    @Nullable
                    override fun getPageTitle(position: Int): CharSequence? {
                        return titleList[position]
                    }

                    override fun getItemId(position: Int): Long {
                        return position.toLong()
                    }

                    override fun getCount(): Int {
                        return titleList.size
                    }
                }
//            communityListPager.adapter = PageAdapter
//            communityListTab.setupWithViewPager(communityListPager)

        }



    }
}