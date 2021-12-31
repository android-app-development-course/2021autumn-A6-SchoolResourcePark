package com.example.schoolresourcepark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.datatype.BmobRelation
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.bumptech.glide.Glide
import com.example.schoolresourcepark.register
import kotlinx.android.synthetic.main.activity_community_details.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.activity_resource_detail.*
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlinx.android.synthetic.main.title.*

class QuestionDetail : AppCompatActivity() {

    val answerList=ArrayList<Answer>()
    var pid:String=""
    var uid=""
    var cid=""
    var from=""
    var isCollected=false
    val mcontext=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        supportActionBar?.hide()
        titleText.setText("问题详情")

        from=intent.getStringExtra("from").toString()//我的提问
        if(from=="my")
        {
            //Toast.makeText(this,"我的提问来的",Toast.LENGTH_SHORT).show()

            pid=intent.getStringExtra("myrid").toString()
            uid=intent.getStringExtra("myuid").toString()
        }
        else{
            from=intent.getStringExtra("Rfrom").toString()//我的回复
            if(from=="my")
            {
                //Toast.makeText(this,"我的提问来的",Toast.LENGTH_SHORT).show()

                pid=intent.getStringExtra("Rmyqid").toString()
                uid=intent.getStringExtra("Rmyuid").toString()
            }
            else
            {
                //社区问题列表
                pid=intent.getStringExtra("pid").toString()
                uid=intent.getStringExtra("uid").toString()
                cid=intent.getStringExtra("cid").toString()
            }
        }
        QD_askintroduction.setMovementMethod(ScrollingMovementMethod.getInstance())


        getObject(pid)
        queryObjects()
        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        //Toast.makeText(this,"执行了查询",Toast.LENGTH_SHORT).show()

//        val adapter=AnswerAdapter(AnswerList)
//        recyclerView.adapter=adapter
        //查询用户是否收藏该问题
        IsCollected()

        answerbutton.setOnClickListener {
            //Toast.makeText(this,pid,Toast.LENGTH_SHORT).show()
            val intent2 = Intent(this, AnswerActivity::class.java)

            intent2.putExtra("proid", pid)
            //intent2.putExtra("comid", cid)
            intent2.putExtra("useid", uid)
            startActivity(intent2)
        }

        val type=0//问题

        bt_collectQue.setOnClickListener {
            if(isCollected==false){
                bt_collectQue.setImageResource(R.drawable.collected)
                //添加关联关系
                collect(uid)
            }
            else{
                bt_collectQue.setImageResource(R.drawable.collect1)
                unCollect(uid)
            }

        }


    }
    private fun initAnswers(){
        repeat(10){
            //answerList.add(Answer("回答者",R.drawable.head, "3min前",R.drawable.answer,"利用position：fixed 固定定位"))
        }
    }
    private fun getObject(objectId: String?) {
        var bmobQuery: BmobQuery<ProblemTable> = BmobQuery()
        bmobQuery.getObject(objectId, object : QueryListener<ProblemTable>() {
            override fun done(problem: ProblemTable?, ex: BmobException?) {
                if (ex == null) {
                    if (problem != null) {
                        val askerId=problem.askid

                        var bmobQuery2: BmobQuery<UserTable> = BmobQuery()
                        bmobQuery2.getObject(askerId, object : QueryListener<UserTable>() {
                            override fun done(user: UserTable?, ex: BmobException?) {
                                if (ex == null) {
                                    //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                                    if(user!=null){

                                        var imageUrlask= user.uimage?.fileUrl.toString()
                                        val strlist = imageUrlask.split("://")
                                        imageUrlask = ""
                                        for (str in strlist)
                                        {
                                            if (str == "http")
                                            {
                                                imageUrlask = imageUrlask + str + "s://"
                                            }
                                            else if (str == "https")
                                            {
                                                imageUrlask = imageUrlask + str + "://"
                                            }
                                            else
                                            {
                                                imageUrlask += str
                                            }
                                        }
                                        Glide.with(mcontext).load(imageUrlask).into(avatarImg)


                                        val askername= user.uname.toString()
                                        asker.text=askername
                                        asktime.text=problem.createdAt.substringBefore(" ")
                                        asktitle.text=problem.qtitle
                                        QD_askintroduction.text=problem.qdetail
                                        Log.e("get", "查询成功" )

                                        val count= problem.qimage?.size
                                        Log.e("size", count.toString())
                                        if (count != null) {
                                            if(count>0){
                                                for(i in 0 until count!!){
                                                    var imageUrl= problem.qimage?.get(i)
                                                    if(imageUrl!=null){
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
                                                        if (imageUrl != null) {
                                                            Log.e("url",imageUrl)
                                                        }
                                                        if(i==0){
                                                            Glide.with(mcontext).load(imageUrl).into(queDetialImg1)
                                                            Log.e("显示图片:","0")
                                                        }
                                                        if(i==1){
                                                            Glide.with(mcontext).load(imageUrl).into(queDetialImg2)
                                                            Log.e("显示图片:","1")

                                                        }
                                                        if(i==2){
                                                            Glide.with(mcontext).load(imageUrl).into(queDetialImg3)
                                                            Log.e("显示图片:","2")

                                                        }
                                                    }

                                                    else{
                                                        Log.e("imageUrl","null")
                                                    }
                                                }
                                            }
                                        }





                                    }
                                } else {
                                    //Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        })

                    }
                } else {
                    //Toast.makeText(mContext, ex.message, Toast.LENGTH_LONG).show()
                    Log.e("get", "查询失败：" + ex.message)
                }
            }
        })
    }
    private fun queryObjects() {
        //val AnswerList=ArrayList<Answer>()
        var bmobQuery: BmobQuery<ReplyTable> = BmobQuery()
        bmobQuery.findObjects(object : FindListener<ReplyTable>() {
            override fun done(replys: MutableList<ReplyTable>?, ex: BmobException?) {

                if (ex == null) {
                    //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                    Log.e("reply", "fail")
                    if (replys != null) {
                        for (reply: ReplyTable in replys) {
                            if(reply.qid==pid)
                            {
                                Log.e("reply", reply.replycontent.toString())

                                var bmobQuery2: BmobQuery<UserTable> = BmobQuery()
                                bmobQuery2.getObject(reply.answerid, object : QueryListener<UserTable>() {
                                    override fun done(user: UserTable?, ex: BmobException?) {
                                        if (ex == null) {
                                            //Toast.makeText(mContext, "查询成功", Toast.LENGTH_LONG).show()
                                            if(user!=null){
                                                val rname= user.uname.toString()
                                                Log.e("name",rname)

                                                var imageUrlREPLY= user.uimage?.fileUrl.toString()
                                                val strlist = imageUrlREPLY.split("://")
                                                imageUrlREPLY = ""
                                                for (str in strlist)
                                                {
                                                    if (str == "http")
                                                    {
                                                        imageUrlREPLY = imageUrlREPLY + str + "s://"
                                                    }
                                                    else if (str == "https")
                                                    {
                                                        imageUrlREPLY = imageUrlREPLY + str + "://"
                                                    }
                                                    else
                                                    {
                                                        imageUrlREPLY += str
                                                    }
                                                }
                                                answerList.add(Answer(rname,imageUrlREPLY,reply.createdAt.substringBefore(" "),R.drawable.answer,reply.replycontent.toString()))
                                                QReplyCount.setText(answerList.size.toString()+"条回复")
                                                val adapter= AnswerAdapter(answerList)
                                                recyclerView.adapter=adapter
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
                    Log.e("reply",ex.message.toString())

                }

            }

        })
    }

//    private fun createOne(type:Int,uid:String,sid:String) {
//        var collect = CollectTable()
//        collect.ctype=type
//        collect.uid=uid
//        collect.sid=sid
//
//        /**
//         * 请不要给 gameScore.objectId 赋值，数据新增成功后将会自动给此条数据的objectId赋值并返回！
//         */
//        collect.save(object : SaveListener<String>() {
//            override fun done(objectId: String?, ex: BmobException?) {
//                if (ex == null) {
//                    //Toast.makeText(context, "新增数据成功：$objectId", Toast.LENGTH_LONG).show()
//                    Log.e("CREATE", "成功")
//                } else {
//                    Log.e("CREATE", "新增数据失败：" + ex.message)
//                }
//            }
//        })
//    }

    private fun collect(objectId: String?) {
        val problem=ProblemTable()
        problem.objectId=pid
        val user = UserTable()
        user.objectId = objectId
        //将当前问题添加到用户表中的collectProblem字段值中，表明当前用户收藏了该问题
        val relation = BmobRelation()
        //将当前问题添加到多对多关联中
        relation.add(problem)
        //多对多关联指向`user`的`collectProblem`字段
        user.collectProblem = relation
        user.update(object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e == null) {
//                    Snackbar.make(btn_like, "多对多关联添加成功", Snackbar.LENGTH_LONG).show()
                    Log.d("QueDetail","收藏成功！")
                    isCollected=true
                } else {
//                    Snackbar.make(btn_like, "多对多关联添加失败：" + e.message, Snackbar.LENGTH_LONG).show()
                    Log.d("QueDetail","收藏失败")
                }
            }
        })
    }

    private fun unCollect(objectId: String?) {
        val user = UserTable()
        user.objectId = objectId
        val pro = ProblemTable()
        pro.objectId=pid
        val relation = BmobRelation()
        relation.remove(pro)
        user.collectProblem = relation
        user.update(object : UpdateListener() {

            override fun done(e: BmobException?) {
                if (e == null) {
//                    Snackbar.make(btn_unlike, "关联关系删除成功", Snackbar.LENGTH_LONG).show()
                    isCollected=false
                    Log.d("QueDetail","取消收藏成功！")
                } else {
//                    Snackbar.make(btn_likes, "关联关系删除失败：" + e.message, Snackbar.LENGTH_LONG).show()
                    Log.d("QueDetail","取消收藏失败")
                }
            }

        })
    }

    companion object{
        fun actionStart(context: Context){
            val intent= Intent(context,QuestionDetail::class.java).apply {
            }
            context.startActivity(intent)
        }
    }

    private fun IsCollected() {
        // 查询这个用户的所有问题，因此查询的是问题表
        val query = BmobQuery<ProblemTable>()
        val user = UserTable()
        user.objectId = uid
        //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("collectProblem", BmobPointer(user))

        query.findObjects(object : FindListener<ProblemTable>() {
            override fun done(problems: List<ProblemTable>, e: BmobException?) {
                if (e == null) {
                    for(pro:ProblemTable in problems){
                        if(pro.objectId.equals(pid)){
                            bt_collectQue.setImageResource(R.drawable.collected)
                            isCollected=true
                        }
                    }
                } else {
                    Log.d("ComDetail","查询出错")
                }
            }
        })
    }

    inner class AnswerAdapter(val answerList: List<Answer>)  :
        RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.name)
            val headREPLY: ImageView = view.findViewById(R.id.headREPLY)
            val time: TextView = view.findViewById(R.id.time)
            val answerImage: ImageView =view.findViewById(R.id.answerImage)
            val answerText: TextView = view.findViewById(R.id.answerText)

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.answer_list_item, parent, false)
            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val answer = answerList[position]
            holder.name.text = answer.name
//            holder.head.setImageResource(answer.head)
            holder.time.text=answer.time
            holder.answerImage.setImageResource(answer.answerImage)
            holder.answerText.text=answer.answerText
            Glide.with(mcontext).load(answer.head).into(holder.headREPLY)

        }
        override fun getItemCount() = answerList.size
    }
}