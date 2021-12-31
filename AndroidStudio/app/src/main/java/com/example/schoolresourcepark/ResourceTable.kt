package com.example.schoolresourcepark

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

class ResourceTable: BmobObject() {   //资源列表
    var rtitle:String? = null   //标题
    var rfile: BmobFile? = null //文件
    var rdetail:String? = null  //详情
    var upid :String? = null    //上传者编号
    var rc:String? = null       //所属社区编号
}