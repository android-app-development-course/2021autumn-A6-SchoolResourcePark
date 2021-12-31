package com.example.schoolresourcepark

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

class CommunityTable: BmobObject() {    //社区列表
    var cname:String? = null        //名称
    var cimage: BmobFile? = null   //头像
    var cnum:Int = 1           //人数
    var founderid:String? = null   //创建人编号
    var briefIntro:String? = null  //简介
}