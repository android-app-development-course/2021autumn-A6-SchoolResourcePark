package com.example.schoolresourcepark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CollectionAdapter (val collectionList: List<Collection>) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val collectiontitle: TextView = view.findViewById(R.id.collectionTitle)
        val collectionname: TextView = view.findViewById(R.id.collectionName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collection_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val collection = collectionList[position]
        holder.collectiontitle.text = collection.title
        holder.collectionname.text= collection.name

    }
    override fun getItemCount() = collectionList.size
}