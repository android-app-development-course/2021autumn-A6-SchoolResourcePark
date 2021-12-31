package com.example.schoolresourcepark

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.datatype.BmobRelation

class UserTable: BmobObject() {    //用户表
    var uname:String? = null      //昵称
    var uimage: BmobFile? = null  //头像
    var usex:Int = 0       //性别 0男 1女
    var uarea:String? = null      //地区
    var usubject:Int = 0   //学科 0理学 1工学 2经济学 3管理学 4医学
    var umail:String? = null      //邮箱
    var upassword:String? = null  //密码
    var joinCom:BmobRelation?=null//关联社区
    var collectProblem:BmobRelation?=null//收藏的问题
    var collectResource:BmobRelation?=null//收藏的资源
}