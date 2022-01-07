package com.example.schoolresourcepark

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import com.bumptech.glide.Glide
import com.example.schoolresourcepark.UriUtils.getFileAbsolutePath
import kotlinx.android.synthetic.main.activity_create_community.*
import kotlinx.android.synthetic.main.title.*
import java.io.File

class CreateCommunity : AppCompatActivity() {

    var filename:BmobFile? = null //存文件
    private val context=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_community)
        supportActionBar?.hide()
        titleText.setText("创建社区")

        val uid=intent.getStringExtra("uid")
        var user=UserTable()
        user.objectId=uid

        bt_createCom.setOnClickListener {
            val cname=createCom_cname.text.toString()
            val briefIntro=createCom_briefIntro.text.toString()
            if(cname=="" || briefIntro=="") {
                Toast.makeText(this, "填写信息不能为空", Toast.LENGTH_LONG).show()
            }else{
                var community=CommunityTable()
                community.cname=cname
                community.briefIntro=briefIntro
                community.founderid=uid
                community.cimage = filename
                community.save(object : SaveListener<String>() {
                    override fun done(objectId: String?, ex: BmobException?) {
                        if (ex == null) {
                            Toast.makeText(context, "新增数据成功!", Toast.LENGTH_LONG).show()
                            //新增用户社区关联表数据
                            //将当前用户添加到user表中的joincom字段值中，表明当前用户加入该社区
                            val relation = BmobRelation()
                            //将当前用户添加到多对多关联中
                            relation.add(community)
                            //多对多关联指向`user`的`joinCom`字段
                            user.joinCom = relation
                            user.update(object : UpdateListener() {
                                override fun done(e: BmobException?) {
                                    if (e == null) {
                                        Log.d("createCom","添加关联成功")

                                    } else {
                                            //Snackbar.make(btn_like, "多对多关联添加失败：" + e.message, Snackbar.LENGTH_LONG).show()
                                        Log.d("createCom","添加关联失败")
                                    }
                                }
                            })
                            val intent = Intent(context,CommunityList::class.java)
                            intent.putExtra("uid",uid)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("CREATE", "新增数据失败：" + ex.message)
                        }
                    }
                })
            }
        }

        bt_uploadcimage.setOnClickListener {
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
            val path = getFileAbsolutePath(this,imageUri)//获取照片路径
            Log.e("path：",path)
            uploadSingle(this,path)

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
        Glide.with(this).load(src).into(imageup);
    }



}