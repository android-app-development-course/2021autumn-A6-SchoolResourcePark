package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_my_reply.*
import kotlinx.android.synthetic.main.activity_my_resource.*
import kotlinx.android.synthetic.main.title.*

class MyResource : AppCompatActivity() {
    private val resourceList=ArrayList<Resource>()
    var uid=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_resource)
        supportActionBar?.hide()
        titleText.setText("我的资源")

        //initResource()
        val layoutManager=LinearLayoutManager(this)
        myResource.layoutManager=layoutManager
//        val adapter=ResourceAdapter(resourceList)
//        myResource.adapter=adapter

        uid=intent.getStringExtra("uid").toString()
        getQue()

//        var titleBack=findViewById<ImageView>(R.id.titleBack)
//        //点击返回个人中心
//        titleBack.setOnClickListener{
//            val intent = Intent(this, PersonalCenter::class.java)
//            startActivity(intent)
//        }

    }
    private fun getQue() {
        //数据查询
        var bmobQuery: BmobQuery<ResourceTable> = BmobQuery()
        bmobQuery.addWhereEqualTo("upid", uid)
        bmobQuery.findObjects(object : FindListener<ResourceTable>() {
            override fun done(ress: MutableList<ResourceTable>?, ex: BmobException?) {
                //val replyList = ArrayList<Reply>()
                if (ex == null) {
//                Toast.makeText(parentFragment?.context, "查询成功", Toast.LENGTH_LONG).show()
                    if (ress != null) {
                        for (res: ResourceTable in ress) {
//                            val cname=reply.cname.toString()
                            var title = res.rtitle.toString()
                            val time = res.createdAt.substringBefore(" ").toString()
                            val resid=res.objectId
                            resourceList.add(Resource(title, time,resid))
                            Log.d("res", resourceList.size.toString())
                            resourceCount.setText(resourceList.size.toString()+"条资源")
                        }
                    }
                } else {
//                Toast.makeText(parentFragment?.context, ex.message, Toast.LENGTH_LONG).show()
                }
                val adapter=ResourceAdapter(resourceList)
                myResource.adapter=adapter
            }

        })
    }
    private fun initResource(){
        repeat(10){
            resourceList.add(Resource("高数一复习资料","发布于2019-2-21",""))
        }
    }


    inner class ResourceAdapter(val resourceList: List<Resource>) :
        RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val resourcetitle: TextView = view.findViewById(R.id.resourceTitle)
            val resourcetime: TextView = view.findViewById(R.id.resourceTime)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.resource_item, parent, false)
            val holder=ViewHolder(view)
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                val ques = resourceList[position]
                val intent = Intent(parent.context, ResourceDetail::class.java)
                intent.putExtra("myrid", ques.RID)
                intent.putExtra("myuid", uid)
                intent.putExtra("from","my")
                startActivity(intent)
                //传数据，再跳转
            }
            return holder


        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val resource = resourceList[position]
            holder.resourcetitle.text = resource.title
            holder.resourcetime.text = resource.time
        }
        override fun getItemCount() = resourceList.size
    }
}