package com.example.schoolresourcepark

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.rotationMatrix
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UploadBatchListener
import cn.bmob.v3.listener.UploadFileListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_create_community.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.answer.*
import kotlinx.android.synthetic.main.title.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class quiz : AppCompatActivity() {
    var filename:BmobFile? = null //存文件
    var i:Int = 1
    var fileUrls:MutableList<String> = ArrayList()//存多个图片的url
    private val SELECT_TO_PHOTO_TAG: Int = 0X001 //选图
    private val TAKE_CAMERA_TAG = 0X002 //拍照
    lateinit var imageUri:Uri
    lateinit var outputImage:File

    private val context=this
    var cid=""
    var uid=""
    var cnum=0
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        supportActionBar?.hide()
        titleText.setText("提问")

        cid=intent.getStringExtra("communityID").toString()
        uid=intent.getStringExtra("userID").toString()
        cnum=intent.getIntExtra("cnum",0)
        bt_uploadProblem.setOnClickListener{
            val inputTitle = problemTitleContext.text.toString()
            val inputContent = add_problem_content.text.toString()
            if(inputTitle!=""&&inputContent!="")
            {
                var problem = ProblemTable()
                problem.askid=uid
                problem.qc=cid
                problem.qtitle=inputTitle
                problem.qdetail=inputContent
                problem.collcount=0
                problem.comecount=0
                problem.qimage=fileUrls
                problem.save(object : SaveListener<String>() {
                override fun done(objectId: String?, ex: BmobException?) {
                    if (ex == null) {
                        Toast.makeText(context, "成功发布提问", Toast.LENGTH_LONG).show()
                        Log.e("CREATE", "成功")
                        problemTitleContext.setText("")
                        add_problem_content.setText("")
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
            else{
                Toast.makeText(this,"标题和内容不能为空",Toast.LENGTH_SHORT).show()
            }
        }


        uploadProblemPhoto.setOnClickListener {
                openMyPhoto()
        }
        CameraLogo.setOnClickListener {
            //openMyCamera
            outputImage=File(externalCacheDir,"output_image.jpg")
            if(outputImage.exists()){
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri=if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
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
    fun openMyPhoto(){
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
            Glide.with(this).load(src).into(question_img1)
        else if (i==2)
            Glide.with(this).load(src).into(question_img2)
        else
            Glide.with(this).load(src).into(question_img3)
    }

    private fun rotateIfRequired(bitmap: Bitmap):Bitmap{
        val exif=ExifInterface(outputImage.path)
        val orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
        return when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90->rotateBitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180->rotateBitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270->rotateBitmap(bitmap,270)
            else->bitmap
        }


    }
    private fun rotateBitmap(bitmap: Bitmap,degree:Int):Bitmap{
        val matrix=Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        bitmap.recycle()
        return rotatedBitmap
    }
}
