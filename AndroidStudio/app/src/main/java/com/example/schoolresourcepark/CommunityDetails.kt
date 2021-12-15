package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_community_details.*
import kotlinx.android.synthetic.main.title.*
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentPagerAdapter

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_resource.*
import java.util.ArrayList

class CommunityDetails : AppCompatActivity() {
    private val titleList = ArrayList<String>()
    private val fragmentList = ArrayList<Fragment>()

    private val resourceList=ArrayList<Resource>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_details)
        supportActionBar?.hide()
        titleText.setText("社区名称")
        tabQuestion.bringToFront()
        tabResource.bringToFront()
        tabQuestion.setOnClickListener{
            val intent = Intent(this, AnswerActivity::class.java)
            startActivity(intent)
        }
        tabResource.setOnClickListener{
            val intent = Intent(this, uploadResources::class.java)
            startActivity(intent)
        }

        titleList.clear();
        titleList.add("问题列表");
        titleList.add("资源列表");
        fragmentList.clear()
        fragmentList.add(ComPagerFragment1())
        fragmentList.add(ComPagerFragment2())



        val PageAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
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
        communityPager.adapter=PageAdapter
        communityTab.setupWithViewPager(communityPager)

    }



}