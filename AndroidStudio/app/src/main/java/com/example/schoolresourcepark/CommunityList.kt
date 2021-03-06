
package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_community_details.*
import kotlinx.android.synthetic.main.activity_community_list.*
import kotlinx.android.synthetic.main.title.*
import java.util.ArrayList

class CommunityList : AppCompatActivity() {
    private val titleList = ArrayList<String>()
    private val fragmentList = ArrayList<Fragment>()
    private var uid:String=""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_list)

        supportActionBar?.hide()
        tabCreateCom.bringToFront()

        uid=intent.getStringExtra("uid").toString()


        tabCreateCom.setOnClickListener{
            val intent=Intent(this,CreateCommunity::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
            finish()

        }

        mybutton.setOnClickListener {
            val intent = Intent(this, PersonalCenter::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
            finish()

        }


//        val communitytab: TabLayout =findViewById(R.id.communityTab)
//        val communitypager: ViewPager =findViewById(R.id.communityPager)
        titleList.clear();
        titleList.add("推荐");
        titleList.add("我的");
        fragmentList.clear()
        fragmentList.add(ComListFragmentg1())
        fragmentList.add(ComListFragmentg2())

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
        communityListPager.adapter=PageAdapter
        communityListTab.setupWithViewPager(communityListPager)
    }

    override fun onResume() {
        super.onResume()
//        val intent=Intent(this,CommunityList::class.java)
//        startActivity(intent)
//        onCreate(null)
    }


    fun sendUid():String {
        return uid
    }
}