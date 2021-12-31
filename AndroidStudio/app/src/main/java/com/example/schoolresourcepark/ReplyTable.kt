package com.example.schoolresourcepark

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

class ReplyTable: BmobObject() {   //回复列表
    var answerid:String? = null        //回复人编号
    var replyimage: MutableList<String>? = null   //回复图片
    var replyfile:MutableList<String>? = null   //回复文件
    var qid:String? = null             //问题编号
    var replycontent:String? = null    //回复内容
}