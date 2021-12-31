package com.example.schoolresourcepark

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UploadFileListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_create_community.*
import kotlinx.android.synthetic.main.activity_upload_resources.*
import kotlinx.android.synthetic.main.title.*
import java.io.File

class uploadResources : AppCompatActivity() {
    var cid=""
    var uid=""
    var name=""
    var filename:BmobFile? = null //存文件
    var cnum=0
    val context=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_resources)

        supportActionBar?.hide()
        titleText.setText("上传资源")
        cid=intent.getStringExtra("communityID").toString()
        uid=intent.getStringExtra("userID").toString()
        cnum=intent.getIntExtra("cnum",0)

        bt_uploadResource.setOnClickListener{
            val inputTitle = resourceTitleContext.text.toString()
            val inputContent = add_REScontent.text.toString()
            if(inputTitle!=""&&inputContent!="")
            {
                Log.e("uid：",uid)
                createOne(uid,cid,inputTitle,inputContent,filename)
                Toast.makeText(this,"成功发布资源", Toast.LENGTH_SHORT).show()
                resourceTitleContext.setText("")
                add_REScontent.setText("")
            }
            else{
                Toast.makeText(this,"标题和简介不能为空", Toast.LENGTH_SHORT).show()
            }


        }
        //选择文件
        chooseTOupload.setOnClickListener {
            Thread(Runnable {
                runOnUiThread{
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"
                    startActivityForResult(intent,1);
                }
            }).start()
        }
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            val fileUri= data?.data
            val path = UriUtils.getFileAbsolutePath(this, fileUri)//获取文件路径
            name=path.substring(path.indexOfLast{it=='/'}+1)  //文件名称
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
        var url = filename?.fileUrl.toString()
        val strlist = url.split("://")
        url = ""
        for (str in strlist)
        {
            if (str == "http")
            {
                url = url + str + "s://"
            }
            else if (str == "https")
            {
                url = url + str + "://"
            }
            else
            {
                url += str
            }
        }
        Log.e("url",url)
        loadFileName()
    }

    private fun loadFileName() {
        fileName.setText(name)
    }


    private fun createOne(uid:String,cid:String,title:String,content:String,file1:BmobFile?) {
        var res = ResourceTable()
        res.upid=uid
        res.rc=cid
        res.rtitle=title
        res.rdetail=content
        res.rfile = file1

        /**
         * 请不要给 gameScore.objectId 赋值，数据新增成功后将会自动给此条数据的objectId赋值并返回！
         */
        res.save(object : SaveListener<String>() {
            override fun done(objectId: String?, ex: BmobException?) {
                if (ex == null) {
                    //Toast.makeText(context, "新增数据成功：$objectId", Toast.LENGTH_LONG).show()
                    Log.e("CREATE", "成功")
                    val intent=Intent(context,CommunityDetails::class.java)
                    intent.putExtra("cid",cid)
                    intent.putExtra("uid",uid)
                    intent.putExtra("cnum",cnum)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("CREATE", "新增数据失败：" + ex.message)
                }
            }
        })
    }
}