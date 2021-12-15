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
import kotlinx.android.synthetic.main.comlistfragment1.*
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlinx.android.synthetic.main.compagerfragment1.comPangerFrag1

class ComListFragmentg1 : Fragment() {
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
        val adapter=CLFragAdapter(getQue())
        comListFrag1.adapter=adapter
    }

    private fun getQue():List<ListCommunity>{
        val queList=ArrayList<ListCommunity>()
        repeat(10){
            queList.add(ListCommunity(1,"华南师范大学计机小组",250))
        }
        return queList
    }

    inner class CLFragAdapter(val ComQueList: List<ListCommunity>):
        RecyclerView.Adapter<CLFragAdapter.ViewHolder>(){
        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
            //val QueImg: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueImg)
            val ComName: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ComName)
            val ComMebCount: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ComMebCount)
        }
        override fun onCreateViewHolder(parent: ViewGroup,view:Int): ViewHolder {
            val view =LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.comlist_item,parent,false)
            val holder=ViewHolder(view)

        holder.itemView.setOnClickListener{
//            val que=ComQueList[holder.adapterPosition]
//                NewsContentActivity.actionStart(parent.context,que.title,que.content)
            val intent= Intent(parent.context,CommunityDetails::class.java)
            startActivity(intent)
        }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val que=ComQueList[position]
//        holder.QueImg.text=que.img
            holder.ComName.text=que.Name
            holder.ComMebCount.text=que.Count.toString()
        }

        override fun getItemCount()=ComQueList.size
    }
}