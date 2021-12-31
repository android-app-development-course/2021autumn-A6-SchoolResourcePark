package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.answer.*
import kotlinx.android.synthetic.main.title.*
import java.sql.Struct

class AnswerActivity : AppCompatActivity() {
    var pid =""
    var uid =""
    //var cid =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.answer)

        supportActionBar?.hide()
        titleText.setText("回答")

        pid =intent.getStringExtra("proid").toString()
        uid =intent.getStringExtra("useid").toString()
        //cid =intent.getStringExtra("comid").toString()
        bt_uploadReply.setOnClickListener{
            val inputContent = add_ReplyContent.text.toString()
            if(inputContent!=""){
                createOne(pid,uid,inputContent)
                Toast.makeText(this,"成功发布回答", Toast.LENGTH_SHORT).show()
                add_ReplyContent.setText("")

            }
            else{
                Toast.makeText(this,"回答内容不能为空", Toast.LENGTH_SHORT).show()

            }

        }


        uploadReplyPhoto.setOnClickListener {
            selectimage()


        }
        openCamera.setOnClickListener {



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

    private fun createOne(pid: String?,uid:String,content:String) {
        var reply = ReplyTable()
        reply.qid=pid
        reply.answerid=uid
        reply.replycontent=content

        /**
         * 请不要给 gameScore.objectId 赋值，数据新增成功后将会自动给此条数据的objectId赋值并返回！
         */
        reply.save(object : SaveListener<String>() {
            override fun done(objectId: String?, ex: BmobException?) {
                if (ex == null) {
                    //Toast.makeText(context, "新增数据成功：$objectId", Toast.LENGTH_LONG).show()
                    Log.e("CREATE", "成功")
                } else {
                    Log.e("CREATE", "新增数据失败：" + ex.message)
                }
            }
        })
    }
}