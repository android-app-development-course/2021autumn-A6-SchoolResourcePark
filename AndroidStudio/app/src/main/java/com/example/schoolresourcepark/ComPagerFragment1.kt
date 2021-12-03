package com.example.schoolresourcepark
import androidx.fragment.app.Fragment
import android.R
import android.widget.TextView
import android.view.View
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.annotation.Nullable
import androidx.annotation.NonNull


class ComPagerFragment1: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(com.example.schoolresourcepark.R.layout.compagerfragment1, container, false)
    }

}

