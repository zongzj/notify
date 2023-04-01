package com.zong.call.bean

class FunctionBean(val name:String ,val icon:Int,val type:Int)

enum class  FunctionType(i: Int) {
    ReadScreen(1),
    ReadChat(2),
    ReadDanMu(3)
}