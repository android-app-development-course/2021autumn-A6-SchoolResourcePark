package com.example.schoolresourcepark

import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.collectfrag1.*
import kotlinx.android.synthetic.main.compagerfragment1.*

class CollectFrag1: Fragment() {
    private var uid=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(com.example.schoolresourcepark.R.layout.collectfrag1, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager= LinearLayoutManager(activity)
        collectRecycle1.layoutManager=layoutManager
        if (activity!=null){
            val mycollection=activity as MyCollection
            uid=mycollection.sendUid()
        }
        getQue()

//        if (activity!=null){
//            val communitylist=activity as CommunityList
//            uid=communitylist.sendUid()
//        }


    }

    private fun getQue(){
        val collList=ArrayList<Collection>()
//        repeat(10){
//            collList.add(Collection("安卓开发","发布于2019-1-1"))
//        }

        val query = BmobQuery<ProblemTable>()
        val user = UserTable()
        user.objectId = uid

        query.addWhereRelatedTo("collectProblem", BmobPointer(user))
        query.findObjects(object : FindListener<ProblemTable>() {
            override fun done(pros: List<ProblemTable>, e: BmobException?) {
                if (e == null) {
//                    Snackbar.make(btn_likes, "查询成功：" + users.size, Snackbar.LENGTH_LONG).show()
                    for (pro:ProblemTable in pros){
                        val timeString="发布于"+pro.createdAt.substringBefore(" ")
                        collList.add(Collection(pro.qtitle.toString(),timeString,pro.objectId.toString()) )
                    }
                } else {
//                    Snackbar.make(btn_likes, "查询失败：" + e.message, Snackbar.LENGTH_LONG).show()
                }
                val adapter=CollectFragAdapter(collList)
                collectRecycle1.adapter=adapter
            }
        })


    }
    inner class CollectFragAdapter(val collectList: List<Collection>):
        RecyclerView.Adapter<CollectFragAdapter.ViewHolder>(){
        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
            val Title: TextView =view.findViewById(com.example.schoolresourcepark.R.id.collectionTitle)
            val Time: TextView =view.findViewById(com.example.schoolresourcepark.R.id.collectionTime)
        }
        override fun onCreateViewHolder(parent: ViewGroup,view:Int): ViewHolder {
            val view =LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.collection_item,parent,false)
            val holder=ViewHolder(view)

            holder.itemView.setOnClickListener{
                val position=holder.adapterPosition
                val coll=collectList[position]
                val intent= Intent(parent.context,QuestionDetail::class.java)
                intent.putExtra("myrid", coll.ID)
                intent.putExtra("myuid", uid)
                intent.putExtra("from","my")
                startActivity(intent)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val coll=collectList[position]
            holder.Title.text=coll.title
            holder.Time.text=coll.time
        }

        override fun getItemCount()=collectList.size
    }
}