package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_my_info.*
import kotlinx.android.synthetic.main.activity_personal_center.*
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.title.*
import kotlinx.android.synthetic.main.title.titleText

class PersonalCenter : AppCompatActivity() {
    var uid=""
    val mcontext=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_center)

        supportActionBar?.hide()
        titleText1.setText("个人中心")

        uid=intent.getStringExtra("uid").toString()

        var bmobQuery: BmobQuery<UserTable> = BmobQuery()
        bmobQuery.getObject(uid, object : QueryListener<UserTable>() {
            override fun done(user: UserTable?, ex: BmobException?) {
                if (ex == null) {
                    //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                    //avatar.setImageResource()
                    if (user != null) {
                        PCusername.text=user.uname.toString()
                        PClocation.text=user.uarea.toString()

                        var imageUrlask= user.uimage?.fileUrl.toString()
                        val strlist = imageUrlask.split("://")
                        imageUrlask = ""
                        for (str in strlist)
                        {
                            if (str == "http")
                            {
                                imageUrlask = imageUrlask + str + "s://"
                            }
                            else if (str == "https")
                            {
                                imageUrlask = imageUrlask + str + "://"
                            }
                            else
                            {
                                imageUrlask += str
                            }
                        }
                        Glide.with(mcontext).load(imageUrlask).into(pc_img)
                        Log.e("get", "查询成功" )
                    }
                } else {
                    //Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
                    Log.e("get", "查询失败：" + ex.message)
                }
            }
        })
        //点击进入个人信息界面
        enter.setOnClickListener{
            val intent = Intent(this, myInfo::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
            finish()
        }

        //点击进入我的收藏界面
        bt_myCollection.setOnClickListener{
            val intent = Intent(this, MyCollection::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
        }
        //点击进入我的提问界面
        bt_myQuestions.setOnClickListener{
            val intent = Intent(this, MyQuestion::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
        }
        //点击进入我的回答界面
        bt_myAnswers.setOnClickListener{
            val intent = Intent(this, MyReply::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
        }
        //点击进入我的资源界面
        bt_myResources.setOnClickListener{
            val intent = Intent(this, MyResource::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
        }
        //点击退出登录
        quit_login.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        communitybutton.setOnClickListener {
            val intent = Intent(this, CommunityList::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
            finish()
        }
    }
}