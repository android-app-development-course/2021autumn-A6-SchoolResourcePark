package com.example.schoolresourcepark

import cn.bmob.v3.BmobObject

class CollectTable: BmobObject() {     //收藏列表
    var ctype:Int? = null   //（0为问题，1为资源）
    var uid:String? = null  //用户编号
    var sid :String? = null //收藏内容编号
}