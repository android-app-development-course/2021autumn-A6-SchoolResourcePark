package com.example.schoolresourcepark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UploadFileListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_reply.*
import java.io.File

class Login : AppCompatActivity() {
    private var flag="false"
    private var bo="false"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //第一：默认初始化
        Bmob.initialize(this, "9221db06757499ed58a8bc4bf5dea96c");
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        //记住密码
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)
        if (isRemember) {
            // 将账号和密码都设置到文本框中
            val account = prefs.getString("account", "")
            val password = prefs.getString("password", "")
            E_mail.setText(account)
            Password.setText(password)
            checkBox.isChecked = true
        }

        val content1=this
        login.setOnClickListener{
            bo="false"
            flag="false"
            val userE=E_mail.text.toString()
            var userP=Password.text.toString()
            if(userE.equals("")||userP.equals("")){
                Toast.makeText(this,"邮箱和密码不能为空！", Toast.LENGTH_LONG).show()
            }else{
                //数据查询
                var bmobQuery: BmobQuery<UserTable> = BmobQuery()
                bmobQuery.addWhereEqualTo("umail", userE)
                bmobQuery.findObjects(object : FindListener<UserTable>() {
                    override fun done(users: MutableList<UserTable>?, ex: BmobException?) {
                        if (ex == null) {
                            if (users != null) {
                                for (user: UserTable in users) {
                                    val upassword = user.upassword.toString()
                                    val uid=user.objectId.toString()
                                    if(userP.equals(upassword)){
                                        val editor = prefs.edit()
                                        if (checkBox.isChecked) { // 检查复选框是否被选中
                                            editor.putBoolean("remember_password", true)
                                            editor.putString("account", userE)
                                            editor.putString("password", upassword)
                                        } else {
                                            editor.clear()
                                        }
                                        editor.apply()
                                        //登陆成功，跳转
                                        val intent = Intent(content1, CommunityList::class.java)
                                        intent.putExtra("uid",uid)
                                        startActivity(intent)
                                        finish()
                                        bo="true"
                                    }else{
                                        Toast.makeText(content1,"用户名或密码错误！", Toast.LENGTH_LONG).show()
                                        E_mail.setText("")
                                        Password.setText("")
                                        flag="true"
                                    }
                                }
                                if("false".equals(flag) && "false".equals(bo)){
                                    Toast.makeText(content1,"用户名或密码错误！", Toast.LENGTH_LONG).show()
                                    E_mail.setText("")
                                    Password.setText("")
                                }
                            }
                        }
                    }
                })
            }
        }

        bt_register.setOnClickListener{
            val intent = Intent(this, register::class.java)
            startActivity(intent)
            finish()
        }
    }

}