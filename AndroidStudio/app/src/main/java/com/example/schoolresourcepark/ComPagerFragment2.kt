package com.example.schoolresourcepark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.R
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import java.util.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlinx.android.synthetic.main.compagerfragment2.*


class ComPagerFragment2: Fragment() {
    private var cid=""
    private var uid=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.schoolresourcepark.R.layout.compagerfragment2, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager=LinearLayoutManager(activity)
        comPangerFrag2.layoutManager=layoutManager
//        val adapter=CPFragAdapter2(getQue())
//        comPangerFrag2.adapter=adapter

        if (activity!=null){
            val communitydetails=activity as CommunityDetails
            cid= communitydetails.sendCid()
            uid= communitydetails.sendUid()
        }

        queryObjects()
    }

    private fun queryObjects() {
        val resourceList=ArrayList<ComResource>()
        var bmobQuery: BmobQuery<ResourceTable> = BmobQuery()
        bmobQuery.findObjects(object : FindListener<ResourceTable>() {
            override fun done(resources: MutableList<ResourceTable>?, ex: BmobException?) {

                if (ex == null) {
                    //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                    if (resources != null) {
                        for (res: ResourceTable in resources) {
                            Log.e("resource", "已读数据")
                            if(res.rc==cid)
                            {
                                Log.e("problem", "插入数据")
                                val rtitle=res.rtitle.toString()
                                val rid=res.objectId
                                val askerid=res.upid
                                val time=res.createdAt.substringBefore(" ")
                                var askername=""
                                var bmobQuery2: BmobQuery<UserTable> = BmobQuery()
                                bmobQuery2.getObject(askerid, object : QueryListener<UserTable>() {
                                    override fun done(user: UserTable?, ex: BmobException?) {
                                        if (ex == null) {
                                            //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                                            if(user!=null){
                                                val releaserImg=user.uimage?.fileUrl.toString()
                                                askername= user.uname.toString()
                                                resourceList.add(ComResource(releaserImg,askername,time,rtitle,rid))

                                                val adapter=CPFragAdapter2(resourceList)
                                                comPangerFrag2.adapter=adapter
                                                Log.e("查询多条数据","成功")
                                            }
                                        } else {
                                            //Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
                                            Log.e("查询多条数据","失败")
                                        }
                                    }
                                })

                            }

                        }
                    }
                } else {
                    Log.e("problem",ex.message.toString())

                }

            }

        })
    }

    inner class CPFragAdapter2(val ComResList: List<ComResource>):
        RecyclerView.Adapter<CPFragAdapter2.ViewHolder>(){
        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
            //val QueImg: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueImg)
            val ResName: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ResName)
            val ResTime: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ResTime)
            val ResContent: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ResContent)
            val ResImg: ImageView =view.findViewById(com.example.schoolresourcepark.R.id.releaserImg)
        }
        override fun onCreateViewHolder(parent: ViewGroup,view:Int): ViewHolder {
            val view =LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.comrecycler2_item,parent,false)
            val holder=ViewHolder(view)
            holder.itemView.setOnClickListener{
                val position=holder.adapterPosition
                val res=ComResList[position]
                val intent= Intent(parent.context,ResourceDetail::class.java)
                intent.putExtra("resid",res.RID)
                intent.putExtra("resuid",uid)
                intent.putExtra("rescid",cid)
                startActivity(intent)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val que=ComResList[position]
//        holder.QueImg.text=que.img
            holder.ResName.text=que.name
            holder.ResTime.text=que.time
            holder.ResContent.text=que.content
            context?.let { Glide.with(it).load(que.img).into(holder.ResImg) }
        }

        override fun getItemCount()=ComResList.size
    }
}