package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.login

class register : AppCompatActivity() {
    private val context=this
    private var flag="false"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //进入登陆界面
        supportActionBar?.hide()

        login.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        register.setOnClickListener{
            flag="false"
            //获取用户的注册信息
            val userName=E_name.text.toString()
            var userEmail=E_emails.text.toString()
            var userPwd=E_password.text.toString()
            var userPlace=E_place.text.toString()
            var userSex=mySex.selectedItemId.toInt()
            var userSubject=mySubject.selectedItemId.toInt()
            if(userName.equals("")||userEmail.equals("")||userPwd.equals("")||userPlace.equals("")){
                Toast.makeText(this,"请填写完整信息！", Toast.LENGTH_LONG).show()
            }else if(userPwd.length<6){
                Toast.makeText(this,"密码长度最少为6位，请重新设置！", Toast.LENGTH_LONG).show()
                E_password.setText("")
            } else{
                //查找该邮箱是否已被注册
                var bmobQuery: BmobQuery<UserTable> = BmobQuery()
                bmobQuery.addWhereEqualTo("umail", userEmail)
                bmobQuery.findObjects(object : FindListener<UserTable>() {
                    override fun done(users: MutableList<UserTable>?, ex: BmobException?) {
                        if (ex == null) {
                            if (users != null) {
                                for (user: UserTable in users) {
                                    Toast.makeText(context, "该邮箱已被注册，请重新输入！", Toast.LENGTH_LONG).show()
                                    E_emails.setText("")
                                    flag="true"
                                }

                                if("false".equals(flag)){  //没有被注册
                                    var user = UserTable()
                                    user.uname=userName   //昵称
                                    user.usex=userSex   //性别
                                    user.umail=userEmail   //邮箱
                                    user.upassword=userPwd   //密码
                                    user.uarea=userPlace   //地区
                                    user.usubject=userSubject   //学科
                                    user.save(object : SaveListener<String>() {
                                        override fun done(objectId: String?, ex: BmobException?) {
                                            if (ex == null) {
                                                Toast.makeText(context, "注册成功！", Toast.LENGTH_LONG).show()
                                                val intent = Intent(context, Login::class.java)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Log.e("sss","${ex.message}")
                                                Toast.makeText(context, "注册失败", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    })
                                }

                            }
                        }
                    }
                })

            }
        }
    }
}