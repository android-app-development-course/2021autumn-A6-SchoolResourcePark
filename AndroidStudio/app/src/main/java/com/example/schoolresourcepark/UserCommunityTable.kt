package com.example.schoolresourcepark

import cn.bmob.v3.BmobObject

class UserCommunityTable: BmobObject("UserCommunityTable") {  //用户社区列表
    var uid:String? = null      //用户编号
    var cid:String? = null      //社区编号
}