package com.example.schoolresourcepark

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.GnssAntennaInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.webkit.DownloadListener
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.activity_resource_detail.*
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlinx.android.synthetic.main.title.*
import kotlinx.android.synthetic.main.activity_personal_center.*
import java.lang.Exception
import java.net.URL
import java.net.URLConnection
import android.os.Environment
import android.os.Handler
import android.os.Message
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.TextView

import android.widget.ProgressBar
import java.io.*


class ResourceDetail : AppCompatActivity() {
    var rid=""
    var uid=""
    var cid=""
    var askername=""

    var from=""
    var isCollected=false
    var file:BmobFile? = null //存文件
    var mcontext=this

    val context = this

    var reFileName = ""

    private val filePath = "/download/"
    private var progressBar: ProgressBar? = null
    private var textView: TextView? = null
    private var DownedFileLength = 0
    private var FileLength = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_detail)
        supportActionBar?.hide()
        checkNeedPermissions()

        progressBar= findViewById(R.id.download_main_progressBarlist)
        textView= findViewById(R.id.download_main_Text)

        titleText.setText("资源详情")

        from=intent.getStringExtra("from").toString()
        if(from=="my")
        {
            //Toast.makeText(this,"我的资源来的",Toast.LENGTH_SHORT).show()

            rid=intent.getStringExtra("myrid").toString()
            uid=intent.getStringExtra("myuid").toString()
        }
        else
        {
            rid=intent.getStringExtra("resid").toString()
            uid=intent.getStringExtra("resuid").toString()
            cid=intent.getStringExtra("rescid").toString()
        }
        RESintroduction.setMovementMethod(ScrollingMovementMethod.getInstance())

        IsCollected()

        var bmobQuery: BmobQuery<ResourceTable> = BmobQuery()
        bmobQuery.getObject(rid, object : QueryListener<ResourceTable>() {
            override fun done(res: ResourceTable?, ex: BmobException?) {
                if (ex == null) {
                    //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                    //avatar.setImageResource()
                    if (res != null) {
                        val releaserId=res.upid
                        file=res.rfile
                        var bmobQuery2: BmobQuery<UserTable> = BmobQuery()
                        bmobQuery2.getObject(releaserId, object : QueryListener<UserTable>() {
                            override fun done(user: UserTable?, ex: BmobException?) {
                                if (ex == null) {
                                    //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                                    if(user!=null){
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
                                        Glide.with(mcontext).load(imageUrlask).into(avatarRES)
                                        askername= user.uname.toString()
                                        RESasker.setText(askername)
                                        REStime.setText(res.createdAt.substringBefore(" "))
                                        REStitle.setText(res.rtitle.toString())
                                        RESintroduction.setText(res.rdetail.toString())
                                        resourceFileName.text = res.rfilename
                                        reFileName = res.rfilename.toString()
                                        Log.e("get", "查询成功" )
                                    }
                                } else {
                                    //Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        })


                    }
                } else {
                    //Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
                    Log.e("get", "查询失败：" + ex.message)
                }
            }
        })


        val type=1//资源

        bt_collectRes.setOnClickListener {
            if(isCollected==false){
                bt_collectRes.setImageResource(R.drawable.collected)
                //插入数据
                collect(uid)
            }
            else{
                bt_collectRes.setImageResource(R.drawable.collect1)
                unCollect(uid)
            }
        }

        resDetial_download.setOnClickListener{
            Log.d("下载资源","here")
            object : Thread() {
                override fun run() {
                    var path: String = file?.url.toString()
                    val strlist = path.split("://")
                    path = ""
                    for (str in strlist)
                    {
                        if (str == "http")
                        {
                            path = path + str + "s://"
                        }
                        else if (str == "https")
                        {
                            path = path + str + "://"
                        }
                        else
                        {
                            path += str
                        }
                    }
                    try {
                        downLoad(path, context)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
    }

    private val handler:Handler = object:Handler()
    {
        override fun handleMessage(msg:Message)
        {
            super.handleMessage(msg)
            if (!Thread.currentThread().isInterrupted()) {
                when(msg.what){
                    0->{
                        progressBar?.max = FileLength;
                    }
                    1->{
                        progressBar?.setProgress(DownedFileLength);
                        var x = DownedFileLength*100/FileLength;
                        textView?.text = "$x%"
                    }
                    2->{
                        Toast.makeText(applicationContext, "下载完成", Toast.LENGTH_LONG).show()
                    }
                    else->{}
                }
            }
        }

    }

    private fun downLoad(path: String, context: Context){

        val url = URL(path)
        //打开连接
        val conn: URLConnection = url.openConnection()
        val iss: InputStream = conn.getInputStream()
        val contentLength = conn.contentLength
        Log.e("", "文件长度 = $contentLength")

        val savePAth = Environment.getExternalStorageDirectory().toString() + filePath
        val file1 = File(savePAth)
        if (!file1.exists()) {
            file1.mkdir()
        }
        val savePathString =
            Environment.getExternalStorageDirectory().toString() + filePath + reFileName
        val file = File(savePathString)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        /*
		 * 向SD卡中写入文件,用Handle传递线程
		 */
        val message = Message()
        try {
            val randomAccessFile = RandomAccessFile(file, "rwd")
            randomAccessFile.setLength(FileLength.toLong())
            val buf = ByteArray(1024*4)
            FileLength=conn.contentLength
            message.what=0
            handler.sendMessage(message)
            var length = 0
            while (iss.read(buf).also { length = it } != -1) {
                randomAccessFile.write(buf,0,length)
                DownedFileLength+=length
                val message1=Message()
                message1.what=1;
                handler.sendMessage(message1)
            }
            iss.close()
            randomAccessFile.close()
            val message2=Message()
            message2.what=2;
            handler.sendMessage(message2)

        } catch (e:FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e:IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

//        //创建文件夹 MyDownLoad，在存储卡下
//        val dirName = Environment.getExternalStorageDirectory().toString() + "/"
//        //下载后的文件名
//        val fileName = dirName + reFileName
//
//        Log.d("文件名：",fileName)
//
//        //打开手机对应的输出流,输出到文件中
//        //创建字节流
//        val buffer = ByteArray(1024)
//        val os: OutputStream = FileOutputStream(fileName)
//        var len = 0
//        //从输入六中读取数据,读到缓冲区中
//        //写数据
//        while (iss.read(buffer).also { len = it } != -1) {
//            os.write(buffer, 0, len)
//        }
//        Log.e("文件不存在", "下载成功！")
//        //关闭输入输出流
//        iss.close()
//        os.close()
    }

    private fun checkNeedPermissions(){
        //6.0以上需要动态申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            //多个权限一起申请
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 1);
        }
    }

    private fun collect(objectId: String?) {
        val resource=ResourceTable()
        resource.objectId=rid
        val user = UserTable()
        user.objectId = objectId
        //将当前问题添加到用户表中的collectProblem字段值中，表明当前用户收藏了该问题
        val relation = BmobRelation()
        //将当前问题添加到多对多关联中
        relation.add(resource)
        //多对多关联指向`user`的`collectProblem`字段
        user.collectResource = relation
        user.update(object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e == null) {
    //                    Snackbar.make(btn_like, "多对多关联添加成功", Snackbar.LENGTH_LONG).show()
                    Log.d("ResDetail","收藏成功！")
                    isCollected=true
                } else {
    //                    Snackbar.make(btn_like, "多对多关联添加失败：" + e.message, Snackbar.LENGTH_LONG).show()
                    Log.d("QueDetail","收藏失败")
                }
            }
        })
    }
    private fun unCollect(objectId: String?) {
        val user = UserTable()
        user.objectId = objectId
        val res = ResourceTable()
        res.objectId=rid
        val relation = BmobRelation()
        relation.remove(res)
        user.collectResource = relation
        user.update(object : UpdateListener() {

            override fun done(e: BmobException?) {
                if (e == null) {
//                    Snackbar.make(btn_unlike, "关联关系删除成功", Snackbar.LENGTH_LONG).show()
                    isCollected=false
                    Log.d("ResDetail","取消收藏成功！")
                } else {
//                    Snackbar.make(btn_likes, "关联关系删除失败：" + e.message, Snackbar.LENGTH_LONG).show()
                    Log.d("ResDetail","取消收藏失败")
                }
            }

        })
    }
    private fun IsCollected() {
        // 查询这个用户的所有问题，因此查询的是问题表
        val query = BmobQuery<ResourceTable>()
        val user = UserTable()
        user.objectId = uid
        //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("collectResource", BmobPointer(user))

        query.findObjects(object : FindListener<ResourceTable>() {
            override fun done(resources: List<ResourceTable>, e: BmobException?) {
                if (e == null) {
                    for(res:ResourceTable in resources){
                        if(res.objectId.equals(rid)){
                            bt_collectRes.setImageResource(R.drawable.collected)
                            isCollected=true
                        }
                    }
                } else {
                    Log.d("ResDetail","查询出错")
                }
            }
        })
    }
}