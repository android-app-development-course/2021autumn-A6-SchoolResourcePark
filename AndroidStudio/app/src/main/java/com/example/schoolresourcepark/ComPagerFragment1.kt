package com.example.schoolresourcepark
import androidx.fragment.app.Fragment
import android.R
import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.view.View
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_my_resource.*
import kotlinx.android.synthetic.main.activity_community_details.*
import kotlinx.android.synthetic.main.compagerfragment1.*


class ComPagerFragment1: Fragment() {
    private var cid=""
    private var uid=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.schoolresourcepark.R.layout.compagerfragment1, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager=LinearLayoutManager(activity)
        comPangerFrag1.layoutManager=layoutManager
        if (activity!=null){
            val communitydetails=activity as CommunityDetails
            cid= communitydetails.sendCid()
            uid= communitydetails.sendUid()
        }
        queryObjects()

    }



    private fun queryObjects() {
        val problemList=ArrayList<ComQuestion>()
        var bmobQuery: BmobQuery<ProblemTable> = BmobQuery()
        bmobQuery.findObjects(object : FindListener<ProblemTable>() {
            override fun done(problems: MutableList<ProblemTable>?, ex: BmobException?) {
                if (ex == null) {
                    //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                    if (problems != null) {
                        for (problem: ProblemTable in problems) {
                            Log.e("problem", "已读数据")
                                if(problem.qc==cid)
                                {
                                    Log.e("problem", "读入数据")
                                    val qtitle=problem.qtitle.toString()
                                    val qccoll=problem.collcount
                                    val qccomm=problem.comecount
                                    val qid=problem.objectId
                                    val askerid=problem.askid
                                    val time=problem.createdAt.substringBefore(" ")
                                    var askername=""
                                    var bmobQuery2: BmobQuery<UserTable> = BmobQuery()
                                    bmobQuery2.getObject(askerid, object : QueryListener<UserTable>() {
                                        override fun done(user: UserTable?, ex: BmobException?) {
                                            if (ex == null) {
                                                //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                                                if(user!=null){
                                                    val img=user.uimage?.fileUrl.toString()
                                                    askername= user.uname.toString()
                                                    problemList.add(ComQuestion(img,askername, time ,qtitle,qccoll,qccomm,qid))
                                                    Log.e("name",askername)
                                                    val adapter=CPFragAdapter(problemList)
                                                    comPangerFrag1.adapter=adapter
                                                }
                                            } else {
                                                //Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
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

inner class CPFragAdapter(val ComQueList: List<ComQuestion>):
    RecyclerView.Adapter<CPFragAdapter.ViewHolder>(){
    inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        //val QueImg: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueImg)
        val QueName: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueName)
        val QueTime: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueTime)
        val QueContent: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueContent)
        val QueCollCount: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueCollCount)
        val QueComCount: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueComCount)
        val QueImg:ImageView=view.findViewById(com.example.schoolresourcepark.R.id.QueImg)
    }
    override fun onCreateViewHolder(parent: ViewGroup,view:Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.comrecycler1_item,parent,false)
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position=holder.adapterPosition
            val ques=ComQueList[position]
            val intent= Intent(parent.context,QuestionDetail::class.java)
            intent.putExtra("pid",ques.QID)
            intent.putExtra("uid",uid)
            intent.putExtra("cid",cid)
            startActivity(intent)

        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val que=ComQueList[position]
//        holder.QueImg.text=que.img
        holder.QueName.text=que.name
        holder.QueTime.text=que.time
        holder.QueContent.text=que.content
        holder.QueCollCount.text=que.countColl.toString()
        holder.QueComCount.text=que.countComment.toString()
        context?.let { Glide.with(it).load(que.askerImg).into(holder.QueImg) }
    }

    override fun getItemCount()=ComQueList.size
}

}

