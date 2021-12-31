package com.example.schoolresourcepark

import android.app.DownloadManager
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import java.io.File
import kotlinx.android.synthetic.main.activity_personal_center.*


class ResourceDetail : AppCompatActivity() {
    var rid=""
    var uid=""
    var cid=""
    var askername=""

    var from=""
    var isCollected=false
    var file:BmobFile? = null //存文件
    var mcontext=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_detail)
        supportActionBar?.hide()

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

        }
    }

    private fun createOne(type:Int,uid:String,sid:String) {
        var collect = CollectTable()
        collect.ctype=type
        collect.uid=uid
        collect.sid=sid

        /**
         * 请不要给 gameScore.objectId 赋值，数据新增成功后将会自动给此条数据的objectId赋值并返回！
         */
        collect.save(object : SaveListener<String>() {
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