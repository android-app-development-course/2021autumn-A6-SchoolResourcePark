package com.example.schoolresourcepark

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CommunityViewPager: ViewPager {
    private var noScroll = true

    constructor(mCtx : Context) : super(mCtx) {

    }

    constructor(mCtx : Context,att : AttributeSet) : super(mCtx,att) {
    }

    fun setScroll(noScroll : Boolean){
        this.noScroll=noScroll
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (noScroll)
            return false
        else
            return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (noScroll)
            return false
        else
            return super.onInterceptTouchEvent(ev)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item,false)//false 标识切花换的时候不需要时间
    }

}