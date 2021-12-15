package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_personal_center.*
import kotlinx.android.synthetic.main.title.*
import kotlinx.android.synthetic.main.title.titleText

class PersonalCenter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_center)

        supportActionBar?.hide()
        titleText1.setText("个人中心")

//        var enterMyInfo=findViewById<ImageView>(R.id.enterLogo)
//        var myCollection=findViewById<Button>(R.id.bt_myCollection)
//        var myQuestions=findViewById<Button>(R.id.bt_myQuestions)
//        var myAnswers=findViewById<Button>(R.id.bt_myAnswers)
//        var myResources=findViewById<Button>(R.id.bt_myResources)
//        var communitylistbutton=findViewById<LinearLayout>(R.id.communitybutton)
//        //点击进入个人信息界面
        enterLogo.setOnClickListener{
            val intent = Intent(this, myInfo::class.java)
            startActivity(intent)
        }
        //点击进入我的收藏界面
        bt_myCollection.setOnClickListener{
            val intent = Intent(this, MyCollection::class.java)
            startActivity(intent)
        }
        //点击进入我的提问界面
        bt_myQuestions.setOnClickListener{
            val intent = Intent(this, MyQuestion::class.java)
            startActivity(intent)
        }
        //点击进入我的回答界面
        bt_myAnswers.setOnClickListener{
            val intent = Intent(this, MyReply::class.java)
            startActivity(intent)
        }
        //点击进入我的资源界面
        bt_myResources.setOnClickListener{
            val intent = Intent(this, MyResource::class.java)
            startActivity(intent)
        }
        communitybutton.setOnClickListener {
            val intent = Intent(this, CommunityList::class.java)
            startActivity(intent)
        }
    }
}