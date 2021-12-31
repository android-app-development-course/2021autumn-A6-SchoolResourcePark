package com.example.schoolresourcepark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.collectfrag1.*
import kotlinx.android.synthetic.main.collectfrag2.*
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlin.collections.Collection

class CollectFrag2: Fragment() {
    private var uid=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(com.example.schoolresourcepark.R.layout.collectfrag2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager= LinearLayoutManager(activity)
        collectRecycle2.layoutManager=layoutManager

        if (activity!=null){
            val mycollection=activity as MyCollection
            uid=mycollection.sendUid()
        }
        getQue()

    }
    private fun getQue(){
        val collList=ArrayList<com.example.schoolresourcepark.Collection>()
        val query = BmobQuery<ResourceTable>()
        val user = UserTable()
        user.objectId = uid

        query.addWhereRelatedTo("collectResource", BmobPointer(user))
        query.findObjects(object : FindListener<ResourceTable>() {
            override fun done(resources: List<ResourceTable>, e: BmobException?) {
                if (e == null) {
//                    Snackbar.make(btn_likes, "查询成功：" + users.size, Snackbar.LENGTH_LONG).show()
                    for (res:ResourceTable in resources){
                        val timeString="发布于"+res.createdAt.substringBefore(" ")
                        collList.add(Collection(res.rtitle.toString(),timeString,res.objectId.toString()) )
                    }
                } else {
//                    Snackbar.make(btn_likes, "查询失败：" + e.message, Snackbar.LENGTH_LONG).show()
                }
                val adapter=CollectFragAdapter2(collList)
                collectRecycle2.adapter=adapter
            }
        })
    }

    inner class CollectFragAdapter2(val collectList: List<com.example.schoolresourcepark.Collection>):
        RecyclerView.Adapter<CollectFragAdapter2.ViewHolder>(){
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
                val intent= Intent(parent.context,ResourceDetail::class.java)
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