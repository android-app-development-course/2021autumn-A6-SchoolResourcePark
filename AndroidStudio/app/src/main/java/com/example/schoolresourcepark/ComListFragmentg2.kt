package com.example.schoolresourcepark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.comlistfragment1.*
import kotlinx.android.synthetic.main.comlistfragment1.comListFrag1
import kotlinx.android.synthetic.main.comlistfragment2.*

class ComListFragmentg2 :Fragment(){

    private var uid=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(com.example.schoolresourcepark.R.layout.comlistfragment1, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager= LinearLayoutManager(activity)
        //获取用户的uid

        if (activity!=null){
            val communitylist=activity as CommunityList
            uid=communitylist.sendUid()
        }
        comListFrag1.layoutManager=layoutManager
        getMyComQue()
    }

    private fun getMyComQue(){
        // 查询用户关联的所有社区，因此查询的是社区表
        val query = BmobQuery<CommunityTable>()
        val user=UserTable()
        user.objectId=uid
        //joinCom是user表中的字段，用来存储用户所有关联的社区
        query.addWhereRelatedTo("joinCom", BmobPointer(user))
        query.findObjects(object : FindListener<CommunityTable>() {
            override fun done(communities: List<CommunityTable>, e: BmobException?) {
                val queList=ArrayList<ListCommunity>()
                if (e == null) {
                    if (communities != null) {
                        for (community: CommunityTable in communities) {
                            val cname=community.cname.toString()
                            val cnum= community.cnum
                            val cid=community.objectId
                            var cimage=community.cimage?.fileUrl.toString()
                            val strlist = cimage.split("://")
                            cimage = ""
                            for (str in strlist)
                            {
                                if (str == "http")
                                {
                                    cimage = cimage + str + "s://"
                                }
                                else if (str == "https")
                                {
                                    cimage = cimage + str + "://"
                                }
                                else
                                {
                                    cimage += str
                                }
                            }
                            queList.add(ListCommunity(cimage,cname,cnum,cid))
                            Log.d("MyComList",queList.size.toString())
                        }
                    }
                } else {
                    Log.d("MyComList","查询失败")

                }
                val adapter=CLFragAdapter(queList)
                comListFrag1.adapter=adapter
            }
        })
    }

    inner class CLFragAdapter(val ComQueList: List<ListCommunity>):
        RecyclerView.Adapter<CLFragAdapter.ViewHolder>(){
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            val ComName: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ComName)
            val ComMebCount: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ComMebCount)
            val ComImg: ImageView =view.findViewById(com.example.schoolresourcepark.R.id.ComImg)
        }
        override fun onCreateViewHolder(parent: ViewGroup, view:Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.comlist_item,parent,false)
            val holder=ViewHolder(view)

            holder.itemView.setOnClickListener{
//            val que=ComQueList[holder.adapterPosition]
//                NewsContentActivity.actionStart(parent.context,que.title,que.content)
                val position=holder.adapterPosition
                val community=ComQueList[position]
                val intent= Intent(parent.context,CommunityDetails::class.java)
                intent.putExtra("cid",community.ID)
                intent.putExtra("cnum",community.Count)
                intent.putExtra("uid",uid)
                startActivity(intent)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val que=ComQueList[position]
//        holder.QueImg.text=que.img
            holder.ComName.text=que.Name
            holder.ComMebCount.text=que.Count.toString()
            context?.let { Glide.with(it).load(que.Img).into(holder.ComImg) };
        }

        override fun getItemCount()=ComQueList.size
    }
}