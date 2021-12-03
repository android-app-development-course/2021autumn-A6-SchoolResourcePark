package com.example.schoolresourcepark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.title.*

class uploadResources : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_resources)

        supportActionBar?.hide()
        titleText.setText("上传资源")
    }
}