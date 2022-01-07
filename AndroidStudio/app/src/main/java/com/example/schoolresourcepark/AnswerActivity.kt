package com.example.schoolresourcepark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UploadFileListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.answer.*
import kotlinx.android.synthetic.main.title.*
import java.io.File
import java.sql.Struct

class AnswerActivity : AppCompatActivity() {
    var pid =""
    var uid =""
    //var cid =""
    private val SELECT_TO_PHOTO_TAG: Int = 0X001 //选图
    private val TAKE_CAMERA_TAG = 0X002 //拍照
    var filename:BmobFile? = null //存文件
    var fileUrls: MutableList<String> = ArrayList() //存多个图片的url
    var i:Int = 1 //决定将图片显示在哪个位置

    lateinit var imageUri: Uri
    lateinit var outputImage:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.answer)

        supportActionBar?.hide()
        titleText.setText("回答")

        pid =intent.getStringExtra("proid").toString()
        uid =intent.getStringExtra("useid").toString()
        problemTitle.text = intent.getStringExtra("titleid").toString()

        //cid =intent.getStringExtra("comid").toString()
        bt_uploadReply.setOnClickListener{
            val inputContent = add_ReplyContent.text.toString()
            if(inputContent!=""){
                createOne(pid,uid,inputContent)
                Toast.makeText(this,"成功发布回答", Toast.LENGTH_SHORT).show()
                add_ReplyContent.setText("")
                val intent=Intent(this,QuestionDetail::class.java)
                intent.putExtra("pid",pid)
                intent.putExtra("uid",uid)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"回答内容不能为空", Toast.LENGTH_SHORT).show()
            }
        }

        uploadReplyPhoto.setOnClickListener {
            selectimage()
        }
        openCamera.setOnClickListener {
            outputImage=File(externalCacheDir,"output_image.jpg")
            if(outputImage.exists()){
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri=if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
                FileProvider.getUriForFile(this,"com.example.schoolresourcepark.fileprovider",outputImage)
            }
            else{
                Uri.fromFile(outputImage)
            }
            //启动
            val intent=Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
            startActivityForResult(intent,TAKE_CAMERA_TAG)
        }
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SELECT_TO_PHOTO_TAG)
        {
            if(resultCode == Activity.RESULT_OK) {
                /**
                 * 当选择的图片不为空的话，在获取到图片的途径
                 */
                val imageUri= data?.data
                val path = UriUtils.getFileAbsolutePath(this, imageUri)//获取照片路径
                uploadSingle(this,path)
            }
        }
        else if(requestCode == TAKE_CAMERA_TAG)
        {
            if(resultCode == Activity.RESULT_OK) {
                val path = UriUtils.getFileAbsolutePath(this, imageUri)//获取照片路径
                Log.e("path::",path.toString())
                uploadSingle(this,path)
            }
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
                startActivityForResult(intent,SELECT_TO_PHOTO_TAG)
            }
        }).start()
    }

    /**
     * 将文件设置到表中
     */
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
        var imageUrl = filename?.url.toString()
//        Log.e("url:",imageUrl)
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
        fileUrls.add(imageUrl)
        loadNetImage(imageUrl)
        i++
//        Log.e("urls:",fileUrls.toString())
    }

    fun loadNetImage(src : String){
        if (i==1)
            Glide.with(this).load(src).into(reply_img1)
        else if (i==2)
            Glide.with(this).load(src).into(reply_img2)
        else
            Glide.with(this).load(src).into(reply_img3)
    }

    private fun createOne(pid: String?,uid:String,content:String) {
        var reply = ReplyTable()
        reply.qid=pid
        reply.answerid=uid
        reply.replycontent=content
        reply.replyimage = fileUrls
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