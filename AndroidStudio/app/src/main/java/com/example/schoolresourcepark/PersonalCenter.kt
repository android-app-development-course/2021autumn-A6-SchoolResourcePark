package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.title.*

class PersonalCenter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_center)

        supportActionBar?.hide()
        titleText.setText("个人中心")

        var enterMyInfo=findViewById<ImageView>(R.id.enterLogo)
        var myCollection=findViewById<Button>(R.id.bt_myCollection)
        var myQuestions=findViewById<Button>(R.id.bt_myQuestions)
        var myAnswers=findViewById<Button>(R.id.bt_myAnswers)
        var myResources=findViewById<Button>(R.id.bt_myResources)
        //点击进入个人信息界面
        enterMyInfo.setOnClickListener{
            val intent = Intent(this, myInfo::class.java)
            startActivity(intent)
        }
        //点击进入我的收藏界面
        myCollection.setOnClickListener{
            val intent = Intent(this, MyCollection::class.java)
            startActivity(intent)
        }
        //点击进入我的提问界面
        myQuestions.setOnClickListener{
            val intent = Intent(this, MyQuestion::class.java)
            startActivity(intent)
        }
        //点击进入我的回答界面
        myAnswers.setOnClickListener{
            val intent = Intent(this, MyReply::class.java)
            startActivity(intent)
        }
        //点击进入我的资源界面
        myResources.setOnClickListener{
            val intent = Intent(this, myResources::class.java)
            startActivity(intent)
        }
    }
}