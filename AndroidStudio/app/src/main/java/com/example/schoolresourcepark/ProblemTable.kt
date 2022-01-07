package com.example.schoolresourcepark

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

class ProblemTable: BmobObject() {         //问题列表
    var qtitle:String? = null      //标题
    var qimage: MutableList<String>? = null   //图片
    var qdetail:String? = null     //详情
    var askid :String? = null      //提问人编号
    var qc:String? = null          //所属社区编号
    var collcount:Int = 0
    var comecount:Int = 0

}