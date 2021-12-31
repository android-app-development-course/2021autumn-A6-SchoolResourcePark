package com.example.schoolresourcepark

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolresourcepark.QuestionDetail.Companion.actionStart

//class ResourceAdapter(val resourceList: List<Resource>) :
//    RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val resourcetitle: TextView = view.findViewById(R.id.resourceTitle)
//        val resourcetime: TextView = view.findViewById(R.id.resourceTime)
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.resource_item, parent, false)
//        val holder=ViewHolder(view)
//        holder.itemView.setOnClickListener {
//            val position = holder.adapterPosition
//            val ques = resourceList[position]
////            val intent = Intent(parent.context, QuestionDetail::class.java)
////            intent.putExtra("pid", ques.QID)
////            intent.putExtra("uid", uid)
////            intent.putExtra("cid", cid)
////            startActivity(intent)
//            //传数据，再跳转
////            val communitydetails=parent.activity as CommunityDetails
////            val uid=MyResource.sendUid()
//            ResourceDetail.actionStart(parent.context,ques.RID,)
//        }
//        return holder
//
//
//    }
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val resource = resourceList[position]
//        holder.resourcetitle.text = resource.title
//        holder.resourcetime.text = resource.time
//    }
//    override fun getItemCount() = resourceList.size
//}
