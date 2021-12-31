package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_info.*
import kotlinx.android.synthetic.main.activity_password_change.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.title.*

class passwordChange : AppCompatActivity() {
    var uid=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_change)

        supportActionBar?.hide()
        titleText.setText("密码修改")
        uid=intent.getStringExtra("uid").toString()
        val content1=this
        bt_changepw.setOnClickListener{
            //获取用户的输入的信息
            val pwd=epwd.text.toString()
            var newpwd=enewpwd.text.toString()
            var newpwdd=enewpwdd.text.toString()
            if(pwd.equals("")||newpwd.equals("")||newpwdd.equals("")){
                Toast.makeText(this,"请填写完整信息！", Toast.LENGTH_LONG).show()
            }else{
                //检查原始密码是否输入正确
                var bmobQuery: BmobQuery<UserTable> = BmobQuery()
                bmobQuery.addWhereEqualTo("objectId", uid)
                bmobQuery.findObjects(object : FindListener<UserTable>() {
                    override fun done(users: List<UserTable>, e: BmobException?) {
                        if (e == null) {
                            for (user: UserTable in users){
                                val up = user.upassword.toString()
                                if(pwd.equals(up)){
                                    if(newpwd.equals(newpwdd)){
                                        //更新数据
                                        var user = UserTable()
                                        user.objectId = uid
                                        user.upassword = newpwd
                                        user.update(object : UpdateListener() {
                                            override fun done(ex: BmobException?) {
                                                if (ex == null) {
                                                    Toast.makeText(content1, "修改成功", Toast.LENGTH_LONG).show()
//                                                    val intent = Intent(content1, myInfo::class.java)
//                                                    startActivity(intent)
                                                } else {
                                                    Toast.makeText(content1, "修改失败！", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        })

                                    }else{
                                        Toast.makeText(content1,"新密码两次输入不一致，请重新输入！", Toast.LENGTH_LONG).show()
                                        enewpwd.setText("")
                                        enewpwdd.setText("")
                                    }
                                }else{
                                    Toast.makeText(content1,"原始密码输入错误，请重新输入！", Toast.LENGTH_LONG).show()
                                    epwd.setText("")
                                }
                            }
                        } else {
                            Log.d("ComDetail","查询出错")
                        }
                    }
                })
            }
        }

    }
}