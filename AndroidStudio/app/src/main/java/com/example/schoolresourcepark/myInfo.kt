package com.example.schoolresourcepark

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_community_details.*
import kotlinx.android.synthetic.main.activity_create_community.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.login
import kotlinx.android.synthetic.main.activity_my_info.*
import kotlinx.android.synthetic.main.activity_password_change.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.title.*
import kotlinx.android.synthetic.main.title.view.*
import java.io.File

class myInfo : AppCompatActivity() {
    private var uid:String=""
    private val context=this
    var filename:BmobFile? = null //存文件
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        supportActionBar?.hide()
        titleText.setText("我的信息")
        uid=intent.getStringExtra("uid").toString()  //获取当前用户id
        showInfo(uid)

        titleBack.setOnClickListener{
            val intent=Intent(this,PersonalCenter::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
            finish()
        }

        //点击进入修改密码界面
        bt_changepw1.setOnClickListener{
            val intent = Intent(this, passwordChange::class.java)
            intent.putExtra("uid",uid)
            startActivity(intent)
        }
        bt_savechange1.setOnClickListener{

            AlertDialog.Builder(this).apply {
                setTitle("提示")
                setMessage("您确认要修改个人信息吗？")
                setCancelable(false)
                setPositiveButton("确认"){
                    dialog,which->
                    updateObject(uid)
                }
                setNegativeButton("取消"){
                    dialog,which->
                }
                show()
            }
        }
        mi_head.setOnClickListener {
            selectimage()
        }
    }

    private fun selectimage() {
        Thread(Runnable {
            runOnUiThread{
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
                //intent.setAction(Intent.ACTION_GET_CONTENT)  //实现相册多选 该方法获得的uri在转化为真实文件路径时Android 4.4以上版本会有问题
                //intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI //直接打开系统相册  不设置会有选择相册一步（例：系统相册、QQ浏览器相册）
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1)
            }
        }).start()

    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            val imageUri= data?.data
            val path = UriUtils.getFileAbsolutePath(this, imageUri)//获取照片路径
            uploadSingle(this,path)

        }
    }

    private fun showInfo(uid: String) {
        val query = BmobQuery<UserTable>()
        query.addWhereEqualTo("objectId", uid)
        query.findObjects(object : FindListener<UserTable>() {
            override fun done(users: List<UserTable>, e: BmobException?) {
                if (e == null) {
                    Log.d("myInfo","查询成功")
                    for (user: UserTable in users){
                        val usname = user.uname.toString()
//                        val ussex = user.usex.toString()
                        val usarea = user.uarea.toString()
//                        val usubjects = user.usubject.toString()
                        val usmail = user.umail.toString()
                        mi_user_account.setText(usname)
                        mi_user_address.setText(usarea)
                        mi_user_email.setText(usmail)
                        mi_spinner_mySex.setSelection(user.usex)
                        mi_user_subject.setSelection(user.usubject)
                        Log.d("myInfo", user.uimage?.fileUrl.toString())

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
                        Glide.with(context).load(imageUrlask).into(mi_head)
//                        context?.let { Glide.with(it).load(user.uimage).into(mi_head) }
                    }
                } else {
                    Log.d("myInfo","查询出错")
                }
            }
        })
    }

    private fun updateObject(uid: String?) {
        var userName=mi_user_account.text.toString()
        var userEmail=mi_user_email.text.toString()
        var userPlace=mi_user_address.text.toString()
        var userSex=mi_spinner_mySex.selectedItemId.toInt()
        var userSubject=mi_user_subject.selectedItemId.toInt()
        if(userName.equals("")||userEmail.equals("")||userPlace.equals("")){
            Toast.makeText(context,"请填写完整信息！", Toast.LENGTH_LONG).show()
        }else{
            //查找该邮箱是否已被注册
            var flag=true
            var bmobQuery: BmobQuery<UserTable> = BmobQuery()
            bmobQuery.addWhereEqualTo("umail", userEmail)
            bmobQuery.findObjects(object : FindListener<UserTable>() {
                override fun done(users: MutableList<UserTable>?, ex: BmobException?) {
                    if (ex == null) {
                        if (users != null) {
                            for (user: UserTable in users) {
                                if(user.objectId!=uid){
                                    Toast.makeText(context, "该邮箱已被注册，请重新输入！", Toast.LENGTH_LONG).show()
                                    flag=false
                                }
                            }

                            if(flag==true){  //没有被注册
                                val user = UserTable()
                                user.objectId=uid
                                user.uname=userName   //昵称
                                user.usex=userSex   //性别
                                user.umail=userEmail   //邮箱
                                user.uarea=userPlace   //地区
                                user.usubject=userSubject   //学科
                                user.uimage=filename
                                user.update(object : UpdateListener() {
                                    override fun done(ex: BmobException?) {
                                        if (ex == null) {
                                            Toast.makeText(context, "修改成功！", Toast.LENGTH_LONG).show()
                                            val intent=Intent(context,PersonalCenter::class.java)
                                            intent.putExtra("uid",uid)
                                            startActivity(intent)
                                            finish()
                                        } else {
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

    private fun uploadSingle(context: Context, path: String?) {
        val file = File(path)
        val bmobFile = BmobFile(file)
        bmobFile.uploadblock(object : UploadFileListener() {
            override fun done(ex: BmobException?) {
                if (ex == null) {
                    Log.e("上传成功：","yes")
                    setFileToTable(bmobFile)
                } else {
                    Log.e("上传失败：","${ex.message}")
                }
            }
        })
    }
    /**
     * 将文件设置到表中
     */
    private fun setFileToTable(bmobFile: BmobFile) {
        filename = bmobFile
        var imageUrl = filename?.fileUrl.toString()
        val strlist = imageUrl.split("://")
        imageUrl = ""
        for (str in strlist)
        {
            if (str == "http")
            {
                imageUrl = imageUrl + str + "s://"
            }
            else if (str == "https")
            {
                imageUrl = imageUrl + str + "://"
            }
            else
            {
                imageUrl += str
            }
        }
        loadNetImage(imageUrl)
    }

    fun loadNetImage(src : String){
        Glide.with(this).load(src).into(mi_head);
    }

}