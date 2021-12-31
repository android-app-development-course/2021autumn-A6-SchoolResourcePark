package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import kotlinx.android.synthetic.main.activity_community_list.*
import kotlinx.android.synthetic.main.activity_my_collection.*
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlinx.android.synthetic.main.title.*

class MyCollection : AppCompatActivity() {
    private val collectionList=ArrayList<Collection>()
    private val titleList = java.util.ArrayList<String>()
    private val fragmentList = java.util.ArrayList<Fragment>()
    var uid=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_collection)

        supportActionBar?.hide()
        titleText.setText("我的收藏")
        uid=intent.getStringExtra("uid").toString()

        //initCollection()
//        val layoutManager= LinearLayoutManager(this)
//        myCollection.layoutManager=layoutManager
//        val adapter=CollectionAdapter(collectionList)
//        myCollection.adapter=adapter
        titleList.clear();
        titleList.add("问题");
        titleList.add("资源");
        fragmentList.clear()
        fragmentList.add(CollectFrag1())
        fragmentList.add(CollectFrag2())

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
        collectPager.adapter=PageAdapter
        collectTab.setupWithViewPager(collectPager)

    }
   
//    private fun initCollection(){
//        repeat(10){
//            collectionList.add(Collection("Linux进程通信","发布于xxx"))
//        }
//    }
    fun sendUid():String{
        return uid;
    }


}