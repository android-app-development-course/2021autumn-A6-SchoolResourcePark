package com.example.schoolresourcepark

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResourceAdapter(val resourceList: List<Resource>) :
    RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resourcetitle: TextView = view.findViewById(R.id.resourceTitle)
        val resourcetime: TextView = view.findViewById(R.id.resourceTime)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resource_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val resource = resourceList[position]
        holder.resourcetitle.text = resource.title
        holder.resourcetime.text = resource.time
    }
    override fun getItemCount() = resourceList.size
}
