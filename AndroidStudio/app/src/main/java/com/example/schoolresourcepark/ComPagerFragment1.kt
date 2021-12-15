package com.example.schoolresourcepark
import androidx.fragment.app.Fragment
import android.R
import android.content.Intent
import android.widget.TextView
import android.view.View
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.annotation.Nullable
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_my_resource.*
import kotlinx.android.synthetic.main.activity_community_details.*
import kotlinx.android.synthetic.main.compagerfragment1.*


class ComPagerFragment1: Fragment() {

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
        val adapter=CPFragAdapter(getQue())
        comPangerFrag1.adapter=adapter

    }

    private fun getQue():List<ComQuestion>{
        val queList=ArrayList<ComQuestion>()
        repeat(10){
            queList.add(ComQuestion(1,"提问者","3min前","请问大家怎么学安卓开发",55,250))
        }
        return queList
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
    }
    override fun onCreateViewHolder(parent: ViewGroup,view:Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.comrecycler1_item,parent,false)
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener{
//            val que=ComQueList[holder.adapterPosition]
//                NewsContentActivity.actionStart(parent.context,que.title,que.content)

            val intent= Intent(parent.context,QuestionDetail::class.java)
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
    }

    override fun getItemCount()=ComQueList.size
}

}

