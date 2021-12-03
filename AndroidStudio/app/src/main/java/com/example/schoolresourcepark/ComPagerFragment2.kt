package com.example.schoolresourcepark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.R
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import java.util.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.compagerfragment2.*


class ComPagerFragment2: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(com.example.schoolresourcepark.R.layout.compagerfragment2, container, false)
    }
//    private var mRecyclerView: RecyclerView? = null
//    private var mLayoutManager: LayoutManager? = null
//    private var mAdapter: RecyclerViewAdapter? = null
//    private var mainView: View? = null
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        mainView = inflater.inflate(com.example.schoolresourcepark.R.layout.compagerfragment2, container, false)
//        initView()
//        return mainView
//    }
//    private fun initView() {
//        mRecyclerView = mainView!!.findViewById(com.example.schoolresourcepark.R.id.recyclerView)
//        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        val data = ArrayList<ResourceList>()
//        for (i in 0..10) {
//            data.add(ResourceList(i,"发布者昵称","3min前","Android开发自我学习笔记"))
//        }
//        mAdapter = RecyclerViewAdapter(data)
//        recyclerView.setLayoutManager(mLayoutManager)
//        recyclerView.setAdapter(mAdapter)
//    }
}