package com.example.schoolresourcepark

import android.app.Person
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.comlistfragment1.*
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlinx.android.synthetic.main.compagerfragment1.comPangerFrag1

class ComListFragmentg1 : Fragment() {
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
        comListFrag1.layoutManager=layoutManager

        if (activity!=null){
            val communitylist=activity as CommunityList
            uid=communitylist.sendUid()
        }
        getQue()
    }

    private fun getQue(){
        //数据查询
        var bmobQuery: BmobQuery<CommunityTable> = BmobQuery()
        bmobQuery.findObjects(object : FindListener<CommunityTable>() {
            override fun done(communities: MutableList<CommunityTable>?, ex: BmobException?) {
                val queList=ArrayList<ListCommunity>()
                if (ex == null) {
//                Toast.makeText(parentFragment?.context, "查询成功", Toast.LENGTH_LONG).show()
                    if (communities != null) {
                        for (community: CommunityTable in communities) {
                            val cname=community.cname.toString()
                            val cnum= community.cnum
                            val cid=community.objectId
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
                            Log.d("ComList",imageUrl)
                            queList.add(ListCommunity(imageUrl,cname,cnum,cid))
                        }
                    }
                } else {
//                Toast.makeText(parentFragment?.context, ex.message, Toast.LENGTH_LONG).show()
                }
                val adapter=CLFragAdapter(queList)
                comListFrag1.adapter=adapter
            }

        })


    }

    inner class CLFragAdapter(val ComQueList: List<ListCommunity>):
        RecyclerView.Adapter<CLFragAdapter.ViewHolder>(){
        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
            //val QueImg: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueImg)
            val ComName: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ComName)
            val ComMebCount: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ComMebCount)
            val ComImg: ImageView=view.findViewById(com.example.schoolresourcepark.R.id.ComImg)
        }
        override fun onCreateViewHolder(parent: ViewGroup,view:Int): ViewHolder {
            val view =LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.comlist_item,parent,false)
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
            holder.ComName.text=que.Name
            holder.ComMebCount.text=que.Count.toString()
            context?.let { Glide.with(it).load(que.Img).into(holder.ComImg) };
        }

        override fun getItemCount()=ComQueList.size
    }
}