package com.example.schoolresourcepark

import android.app.ProgressDialog.show
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_community_details.*
import kotlinx.android.synthetic.main.title.*
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentPagerAdapter

import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException

import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_personal_center.*
import java.util.ArrayList

class CommunityDetails : AppCompatActivity() {
    private val titleList = ArrayList<String>()
    private val fragmentList = ArrayList<Fragment>()
    var cid:String=""
    var cnum:Int=0
    var uid:String=""
    val mcontext=this
//    private val context=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_details)
        supportActionBar?.hide()
        titleText.setText("社区详情")

        tabQuestion.bringToFront()
        tabResource.bringToFront()
        cid= intent.getStringExtra("cid").toString()
        cnum=intent.getIntExtra("cnum",0)
        uid=intent.getStringExtra("uid").toString()
        comDetal_briefIntro.setMovementMethod(ScrollingMovementMethod.getInstance())

        //查询用户是否已加入该社区

//        var bmobQuery1: BmobQuery<UserCommunityTable> = BmobQuery()
//        bmobQuery1.addWhereEqualTo("cid",cid)
//        bmobQuery1.addWhereEqualTo("uid",uid)
//        bmobQuery1.findObjects(object : FindListener<UserCommunityTable>() {
//            override fun done(ucts: MutableList<UserCommunityTable>?, ex: BmobException?) {
//                if (ex == null) {
////                Toast.makeText(parentFragment?.context, "查询成功", Toast.LENGTH_LONG).show()
//                    if (ucts != null) {
//                        if(ucts.size!=0){
//                            bt_joinCom.setText("已加入")
//                        }
//                    }
//                } else {
//                }
//            }
//        })

        val query = BmobQuery<CommunityTable>()
        val user = UserTable()
        user.objectId = uid
        //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("joinCom", BmobPointer(user))

        query.findObjects(object : FindListener<CommunityTable>() {
            override fun done(communities: List<CommunityTable>, e: BmobException?) {
                if (e == null) {
                    for(com:CommunityTable in communities){
                        if(com.objectId.equals(cid)){
                            bt_joinCom.isClickable=false
                            bt_joinCom.text="已加入"
                        }
                    }
                } else {
                    Log.d("ComDetail","查询出错")
                }
            }
        })


        //查询community数据
        var bmobQuery2: BmobQuery<CommunityTable> = BmobQuery()
        bmobQuery2.getObject(cid, object : QueryListener<CommunityTable>() {
            override fun done(community: CommunityTable?, ex: BmobException?) {
                if (ex == null) {
//                    Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                    if(community!=null)
                    {
                        comDetail_cname.setText(community?.cname.toString())
                        comDetal_cnum.setText(community?.cnum.toString())
                        comDetal_briefIntro.setText(community?.briefIntro.toString())

                        var imageUrl= community.cimage?.fileUrl.toString()
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
                        Glide.with(mcontext).load(imageUrl).into(comDetail_cimage)
                        Log.e("get", "社区不空：" )
                    }


                } else {
//                    Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
                    Log.e("get", "社区空，查询失败" )
                }
            }
        })

        bt_joinCom.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("提示")
                setMessage("您确认要加入该社区吗？")
                setCancelable(false)
                setPositiveButton("确认"){
                    dialog,which->
                    //更新社区人数
                    var community = CommunityTable()
                    community.objectId = cid
                    community.cnum = cnum + 1
                    community.update(object : UpdateListener() {
                        override fun done(ex: BmobException?) {
                            if (ex == null) {
                                Toast.makeText(context, "加入成功", Toast.LENGTH_LONG).show()
                                comDetal_cnum.setText(community.cnum.toString())
                                bt_joinCom.setText("已加入")
                                bt_joinCom.isClickable=false
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
                                            Log.d("ComDetial","添加关联成功")

                                        } else {
//                                            Snackbar.make(btn_like, "多对多关联添加失败：" + e.message, Snackbar.LENGTH_LONG).show()
                                            Log.d("ComDetail","添加关联失败")
                                        }
                                    }
                                })

                            } else {
                                Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
                            }
                        }

                    })



                }
                setNegativeButton("取消"){
                    dialog,which->
                }
                show()
            }

        }

        tabQuestion.setOnClickListener{
            val intent1 = Intent(this, quiz::class.java)
            intent1.putExtra("communityID",cid)
            intent1.putExtra("userID",uid)
            intent1.putExtra("cnum",cnum)
            startActivity(intent1)
            finish()
        }
        tabResource.setOnClickListener{
            val intent2 = Intent(this, uploadResources::class.java)
            intent2.putExtra("communityID",cid)
            intent2.putExtra("userID",uid)
            intent2.putExtra("cnum",cnum)
            startActivity(intent2)
            finish()
        }

        titleList.clear();
        titleList.add("问题列表");
        titleList.add("资源列表");
        fragmentList.clear()
        fragmentList.add(ComPagerFragment1())
        fragmentList.add(ComPagerFragment2())



        val PageAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(i: Int): Fragment {
                return fragmentList[i]
            }

            @Nullable
            override fun getPageTitle(position: Int): CharSequence? {
                return titleList[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount(): Int {
                return titleList.size
            }
        }
        communityPager.adapter=PageAdapter
        communityTab.setupWithViewPager(communityPager)

    }
    fun sendCid(): String {
        return cid
    }
    fun sendUid(): String {
        return uid
    }

}