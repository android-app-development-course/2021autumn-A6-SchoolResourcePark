package com.example.schoolresourcepark

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.bottom_fragment.*





class CommunityAndMine : AppCompatActivity() {

    var currentFragment:Fragment= Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_and_mine)
        supportActionBar?.hide()
        val fragmentManager=supportFragmentManager
        val transaction=fragmentManager.beginTransaction()
        val communityListFragment=CommunityListFragment()
        val personnalCenterFragment=PersonnalCenterFragment()
        transaction.add(R.id.topfragment,communityListFragment)
        transaction.add(R.id.topfragment,personnalCenterFragment)
        transaction.show(personnalCenterFragment)
        transaction.hide(communityListFragment)
        currentFragment=communityListFragment
        transaction.commit()
        communitybutton.setOnClickListener{

            communityLogo.setImageResource(R.drawable.community)
            communityText.setTextColor(Color.parseColor("#6495ED"))
            myLogo.setImageResource(R.drawable.my_grey)
            myText.setTextColor(Color.parseColor("#8A8A8A"))
//            transaction.add(R.id.topfragment,communityListFragment)
            transaction.hide(personnalCenterFragment)
            transaction.show(communityListFragment)
//            currentFragment=communityListFragment
            transaction.commit()

        }
        mybutton.setOnClickListener{
            communityLogo.setImageResource(R.drawable.community_grey)
            communityText.setTextColor(Color.parseColor("#8A8A8A"))
            myLogo.setImageResource(R.drawable.my)
            myText.setTextColor(Color.parseColor("#6495ED"))
//            replaceFragment(PersonnalCenterFragment())
//            transaction.add(R.id.topfragment,communityListFragment)
//            transaction.add(R.id.topfragment,personnalCenterFragment)
            if(communityListFragment!=null)
                transaction.hide(communityListFragment)
//            if(personnalCenterFragment!=null)
//                transaction.show(personnalCenterFragment)
//            currentFragment=personnalCenterFragment
            transaction.commit()
        }

    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentManager=supportFragmentManager
        val transaction=fragmentManager.beginTransaction()
        transaction.replace(R.id.topfragment,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}